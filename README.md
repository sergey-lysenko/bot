# WebDriver Bot PoC repository #

Universal Bot for Exploratory testing based on Selenium WebDriver

## Software prerequisites ##

* Java JDK 11 or later
* Maven 3.x
* Eclipse or other IDE by your preference

## Required Configuration for local executions ##

* On linux, make a copy of ./env.bash (preferably named ./_env.bash as it is already included in the .gitignore file) and fill in CI, TEST and other variables with proper values
* On windows, same environment variables should be added in order to have ability to execute tests from command line

## Environment variables ##

Name|Meaning|Default value
---|---|---
CI|CI environment?|true
TEST|Name of test configuration|bot

CI environment variable should be defined (to any value) to trigger the logic of automatic closing of browser after tests execution.

"Name of test configuration" corresponds to the property file name in the */src/test/resources* directory

## Test configurations ##

Files in */src/test/resources* directory are test configurations. These are standard Java property files with two types of records:

* **_parameter = value** are several test parameters explained below
* **scenario.name = x.x** where double **x.x** defines the chance of particular scenario to be executed.

Omitted scenarios considered to have 0 chance of being selected for execution, initially. However, it is not required to have each and every scenario mentioned in this configuration file in order for them to be executed.

## _include_upstream and _include_downstream parameters ##

To execute a single scenario,

1. add this scenario to the configuration, for example *works.lysenko.scenarios.google.character.Enter = 1.0*
2. add *_include_upstream = true* parameter in order to automatically add the same weight coefficient to all parent scenarios

To execute a subset of scenarios,

1. add the scenario which is the root of the group to be executed, for example *works.lysenko.scenarios.Google = 1.0*
2. add *_include_downstream = true* parameter in order to automatically add the same weight coefficient to all subsequent scenarios

See examples in the */src/test/resources* directory

## _cycles parameter ##

This parameter used by works.lysenko.Cycles class as a number of times to repeat test cycle. General idea is that by using different number of executions it is possible to have minimal acceptance tests in case of low number of executions, regression tests in case of middle number of cycles, and stability tests in case of high cycles count. While exactly the same code base user for all types of testing, therefore less maintenance and tooling variance.

## _conjoint and _debug parameters ##

These are two boolean parameters.

First one is not used by framework part, and is for use within tests only, to distinguish two different modes of test execution, which will be described later. 

Setting the _debug parameter to 'true' adds more diagnostic information to console:

1. Each line in console log prepended by time in milliseconds passed since previous log record.
2. Each interaction with shared data storage dumps snapshot of that data into the console log and as a file into /target/runs directory.

## Used Software components overview ##

**Selenium Webdriver** is used for interfacing with Web Browsers https://www.selenium.dev/

**JUnit Jupiter** is used as test launching platform https://junit.org/junit5/docs/current/user-guide/

**WebDriverManager** is used for automation of getting webdriver executables https://github.com/bonigarcia/webdrivermanager

Several **Apache Commons** libraries are used, namely **commons-math3**, **commons-lang3**, **commons-collections4** as extensions to standard Java APIs https://commons.apache.org/

**JavaFaker** is used for generation of test data https://github.com/DiUS/java-faker

(refer to pom.xml for more details)

## Testing process overview ##

Each test "physically" is an execution of Bot.java by means of JUnit5 and Selenium WebDriver. To execute a test, put up required values into modified local copy *./_env.bash* file and execute the following command: `. ./_env.bash; mvn clean install`

Bot.java itself consists of four simple parts:

1. setupTest() - create the shared test execution context and root set of Leaf Scenarios
2. init() - perform logging into the application
3. cycles.execute() - perform requested amount of testing cycles
4. quit() - write updated default configuration, write execution stats to the console, write json file for ELK import, and finally close the test Browser (or do not close, depending on the configuration)

It is possible to have **_cycles = 0**. In this case, only init() will be executed.

Each test cycle (Step 3) consists of several stages:

* defining the root set of scenario for execution (done by *Cycles.execute()*)
* performing the selection (based on the weight coefficients defined in the configuration file) of one of scenarios from this group

In turn, selection of a scenario from a set (done by Scenarios.execute()) is a sequence of:

1. Creating the list of execution candidates
2. Algorithmical selection of a candidate (based on weight coefficient) 
3. Verification of prerequisites for preselected candidate. If prerequisites are not met, preselected candidate is rejected, removed from list of candidates, and mathematical selection (step 2) performed again. If prerequisites are satisfied, selected scenario is executed.
4. Steps 2 and 3 repeated until one of three happens: 1) Mathematically selected scenario meets prerequisites, 2) maximum amount of retries done (defined by works.lysenko.Constants.DEFAULT_SUFFICIENCY_RETRIES), or 3) list of candidates become empty. By design, all mentioned problems are not exceptional. Warnings and Notices are posted to console and saved in json result file.

Now, there are three possibilities:

1. Selected scenario is Node Scenario (Node have nested scenarios, one of which will be selected for further execution)
2. Selected scenario is Leaf Scenario (Leaf scenario have no nested ones, and the execution of a cycle is complete after it's selection and execution)
2. Selected scenario is Mono Scenario (Special case of Leaf scenario, which only allowed to be executed once)

Execution of Node Scenario (see AbstractNodeScenario.java) is:

1. super.execute() - execution of common AbstractScenario.execute() (technical stuff like writting title in log, renewing current scenario references, etc.)
2. action() - performing Scenario Actions (for example - selection of 'Solutions' from navigation bar)
3. scenarios.execute() - selecting a nested scenario from a set, same as described above
4. finals() - final actions for a scenario
5. done() - technical finalization of scenario

Execution of Leaf scenario (see AbstractLeafScenario.java) is:

1. super.execute() - execution AbstractScenario.execute() (see above)
2. action() - performing Scenario Actions (for example - deletion of a Solution)
3. finals() - final actions for a scenario
4. done() - technical finalization of scenario

## Scenarios graph ##

Scenarios are structured as a common graph. This graph have a set of level-1 Elements which used by Cycles class for initiation of each consequenr test execution. All elements could be of any type, but only Node scenarios are used for linking with next-level scenarios. In the current implementation, test examples, and result visualization, assumes that scenarios are organized as tree. However, it is already possible to create graph-like interlinking. Later implementations of framework will support graph scenarios in more fully.

Each test cycle is a random weighted and prerequisited selection of several Node scenarios and single ending Leaf Scenario, for example:

Node 'Elements' >> Node 'Edit' >> Leaf 'Delete'

Each scenario, either of Node, Leaf or Mono type, can have or not have any Actions associated with it (however, while it is possible to imagine a Node scenario which was created only for taxonomy reasons and therefore there is no actions in it, it is rather illogical to have a Leaf scenario without any Actions).

## Scenario Prerequisites ##

Each scenario class could have Boolean sufficed() method redefined in order to indicate readiness of the Scenario to be executed. All scenarios are not sufficed by default.

Recommended way to manage the state of the system is by sharing the data through Properties Run.data object.
