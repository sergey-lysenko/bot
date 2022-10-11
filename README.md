# WebDriver Bot PoC repository #

Universal Bot for Exploratory testing based on Selenium WebDriver

## Software prerequisites ##

* Java JDK 11 or later
* Maven 3.x
* Eclipse or other IDE by your preference

## Test Execution ##

Use  *./bot.sh* or *./bot.cmd* scripts to start local test execution. 
These scripts are one-liners for staring of the Maven build with required parameters.

## Test Parameters ##

There are several parameters to be defined before execution of a test.
It is possible to define exact values of these parameters by either setting up Environment Variables, or by typing them into the Test Parameters dialogue box shown before test execution.
It is intended to use only Enviroment Variables for defining test parameters during execution within CI pipeline.
Values of parameters are persisted between consequent executions in */target/parameters* file.
Parameter value defined as Environment Variable has greater priority then the one persisted in the */target/parameters* cache.
However, it is still possible to change all values before actual test execution in the Test Parameters dialogue.

Name|Meaning|Default value
---|---|---
CI|CI environment?|true
IWAIT|Implicit wait in seconds. This is a period of time spent by WebDriver implicitly during page navigation.|1
EWAIT|Explicit wait in seconds. This is effectively a Timeout configuration for long waits. For example, default value of 30 seconds is not enough to have Lendfast test passed|30
TEST|Name of test configuration to be executed|**no default value**
DOMAIN|Target deployment address|**no default value**
EMAIL|login for primary test account|**no default value**
PASSWORD|password for primary test account|**no default value**

**CI** environment variable should be defined (to any value) to simulate behavior in CI/pipelines. There will be no GUI for parameters input. Browser will be automatically closed after tests execution. 

**Name of test configuration** corresponds to the file name (without *.test* extension) in the */qa/src/test/resources/tests* directory

## Test configurations ##

Files in */qa/src/test/resources/tests* directory are various test configurations. These are standard Java property files with two types of records:

* **_parameter = value** are several test parameters explained below
* **scenario.name = x.x** where double **x.x** defines the chance of particular scenario to be executed.

Omitted scenarios considered to have 0.0 chance of being selected for execution. However, it is not required to have each and every scenario mentioned in this configuration file in order for them to be executed.

Name|Meaning|Default value
---|---|---
_root|package to start execution from. This is the single required parameter|**no default value**
_cycles|Number of test repetitions|1
_include_upstream|Whether to propagate weights of defined scenarios to their ancestors|false
_include_downstream|Whether to propagate weights of defined scenarios to their descendants|false
_debug|Include additional debug information into log output|false
*fully.qualified.class.Name*|Weight coefficient for a scenario|0.0

### _include_upstream and _include_downstream parameters ###

To execute a single scenario,

1. add this scenario to the configuration, for example *works.lysenko.scenarios.Character = 0.01*
2. add *_include_upstream = true* parameter in order to automatically add the same weight coefficient to all parent scenarios

To execute a subset of scenarios,

1. add the scenario which is the root of the group to be executed, for example *works.lysenko.scenarios.Quote = 0.01*
2. add *_include_downstream = true* parameter in order to automatically add the same weight coefficient to all subsequent scenarios

## Sourcecode overview ##

Location|Description
---|---
src/test/java/works/lysenko/bot|Location of the main Bot test java class
src/test/java/works/lysenko/scenarios|Generic Bot test scenarios
src/main/java/works/lysenko/*|Wrapper library which provides additional functionality to standard WebDriver API

## Used Software components overview ##

**Selenium Webdriver** is used for interfacing with Web Browsers https://www.selenium.dev/

**JUnit Jupiter** is used as test launching platform https://junit.org/junit5/docs/current/user-guide/

**WebDriverManager** is used for automation of getting webdriver executables https://github.com/bonigarcia/webdrivermanager

Several **Apache Commons** libraries are used, namely **commons-math3**, **commons-lang3**, **commons-collections4** as extensions to standard Java APIs https://commons.apache.org/

**JavaFaker** is used for generation of test data https://github.com/DiUS/java-faker

(refer to pom.xml for more details)

## Testing process overview ##

Each test "physically" is an execution of BotTest.java by means of JUnit5 and Selenium WebDriver. To execute a test, put up required values into modified local copy *./_env.bash* file and execute the following command: `. ./_env.bash; mvn clean install`

BotTest.java itself consists of four simple steps:

1. setupTest() - create the shared test execution context and root set of Leaf Scenarios
2. init() - perform logging into the application
3. cycles.execute() - perform requested amount of testing cycles
4. quit() - write updated default configuration, write execution stats to the console, write json file for ELK import, and finally close the test Browser (or do not close, depending on the configuration)

It is possible to have **cycles = 0**. In this case, only init() will be executed.

Each test cycle (Step 3) consists of several stages:

* defining the root set of scenario for execution (done by *Cycles.execute()*) 
* performing the selection (based on the weight coefficients defined in the configuration file) of one of scenarios from this group

In turn, selection of a scenario from a set (done by Scenarios.execute()) is a sequence of:

1. Creating the list of execution candidates
2. Algorithmical selection of a candidate (based on weight coefficient) 
3. Verification of prerequisites for preselected candidate. If prerequisites are not met, preselected candidate is rejected, removed from list of candidated, and mathematical selection (step 2) performed again. If prerequisites are satisfied, selected scenario is executed.
4. Steps 2 and 3 repeated until one of three happens: 1) Mathematically selected scenario meets prerequisites, 2) maximum amount of retries done (currently - 5, defined as constant in the source code), or 3) list of candidates become empty

By design, all mentioned problems are not exceptional. Warnings and Notices are posted to console and saved in json result file.

Now, there are two possibilities:

1. Selected scenario is Node Scenario (Node have nested scenarios, one of which will be selected for further execution)
2. Selected scenario is Leaf Scenario (Leaf scenario have no nested ones, and the execution of a cycle is complete after it's selection and execution)
2. Selected scenario is Mono Scenario (Special case of Leaf scenario, which only allowed to be executed once)

Execution of Node Scenario (see AbstractNodeScenario.java) is:

1. super.execute() - execution of common AbstractScenario.execute() (technical stuff like writting title in log, renewing current scenario references, etc.)
2. action() - performing Scenario Actions (for example - selection of 'Solutions' from navigation bar)
3. scenarios.execute() - selecting a nested scenario from a set, same as described above
4. finals() - final actions for a scenario
5. done() - technical finalization of scenario

Execution of Leaf scenario (see AbstractNodeScenario.java) is:

1. super.execute() - execution AbstractScenario.execute() (see above)
2. action() - performing Scenario Actions (for example - deletion of a Solution)
3. finals() - final actions for a scenario

## Scenarios graph ##

Scenarios are structured as a graph, which starts from the set of root Node elements and ends by one-level layer of Leaf elements.

Each test cycle is a random weighted and requisited selection of sequence of several Node scenarios and single ending Leaf Scenario, for example:

Node 'Elements' >> Node 'Edit' >> Leaf 'Delete'

Each scenario, either of Node, Leaf or Mono type, can have or not have any Actions associated with it (however, while it is possible to imagine a Node scenario which was created only for taxonomy reasons and therefore there is no actions in it, it is rather illogical to have a Leaf scenario without any Actions).

## Scenario Prerequisites ##

Each scenario class could have Boolean sufficed() method redefined in order to indicate readiness of the Scenario to be executed. Node scenarios are sufficed by default, while Leaf scenarios are not sufficed by default. This is done to 'enforce' definition of sufficed() method for Leaf scenarios.

Recommended way to manage the state of the system is by sharing the data through Properties Run.data object (see scenarios/solutions/create/Create.java for an example)

## Branches and contributions ##

* Make you ongoing commits either to **dev** branch or to a **feature_branch**, upon your judgement
* Contents of **main** branch treated as a release which is ready to be used for tests
