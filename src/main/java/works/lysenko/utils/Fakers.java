package works.lysenko.utils;

import com.github.javafaker.Faker;

import works.lysenko.Common;

/**
 * This is a set of wrapper routines of com.github.javafaker library
 * 
 * @author Sergii Lysenko
 */
public class Fakers {

	/**
	 * @return random quote from one of Fakers
	 */
	public static String quote() {
		String quote = null;
		int c = Common.integer(1, 21);
		switch (c) {
		case 1:
			quote = new Faker().backToTheFuture().quote();
			break;
		case 2:
			quote = new Faker().buffy().quotes();
			break;
		case 3:
			quote = new Faker().dune().quote();
			break;
		case 4:
			quote = new Faker().elderScrolls().quote();
			break;
		case 5:
			quote = new Faker().friends().quote();
			break;
		case 6:
			quote = new Faker().gameOfThrones().quote();
			break;
		case 7:
			quote = new Faker().harryPotter().quote();
			break;
		case 8:
			quote = new Faker().hitchhikersGuideToTheGalaxy().quote();
			break;
		case 9:
			quote = new Faker().hobbit().quote();
			break;
		case 10:
			quote = new Faker().howIMetYourMother().quote();
			break;
		case 11:
			quote = new Faker().leagueOfLegends().quote();
			break;
		case 12:
			quote = new Faker().lebowski().quote();
			break;
		case 13:
			quote = new Faker().overwatch().quote();
			break;
		case 14:
			quote = new Faker().princessBride().quote();
			break;
		case 15:
			quote = new Faker().rickAndMorty().quote();
			break;
		case 16:
			quote = new Faker().robin().quote();
			break;
		case 17:
			quote = new Faker().shakespeare().asYouLikeItQuote();
			break;
		case 18:
			quote = new Faker().shakespeare().hamletQuote();
			break;
		case 19:
			quote = new Faker().shakespeare().kingRichardIIIQuote();
			break;
		case 20:
			quote = new Faker().shakespeare().romeoAndJulietQuote();
			break;
		case 21:
			quote = new Faker().yoda().quote();
			break;
		default:
			quote = "";
			break;
		}
		return quote;
	}

	/**
	 * @param maxLength
	 * @return random quote with limited length
	 */
	public static String quote(int maxLength) {
		String s = quote();
		return s.substring(0, Math.min(s.length(), maxLength));
	}

	/**
	 * @return random character from one of Fakers
	 */
	public static String character() {
		String quote = null;
		int c = Common.integer(1, 19);
		switch (c) {
		case 1:
			quote = new Faker().backToTheFuture().character();
			break;
		case 2:
			quote = new Faker().buffy().characters();
			break;
		case 3:
			quote = new Faker().dune().character();
			break;
		case 4:
			quote = new Faker().elderScrolls().creature();
			break;
		case 5:
			quote = new Faker().friends().character();
			break;
		case 6:
			quote = new Faker().gameOfThrones().character();
			break;
		case 7:
			quote = new Faker().harryPotter().character();
			break;
		case 8:
			quote = new Faker().hitchhikersGuideToTheGalaxy().character();
			break;
		case 9:
			quote = new Faker().hobbit().character();
			break;
		case 10:
			quote = new Faker().howIMetYourMother().character();
			break;
		case 11:
			quote = new Faker().lebowski().character();
			break;
		case 12:
			quote = new Faker().lordOfTheRings().character();
			break;
		case 13:
			quote = new Faker().overwatch().hero();
			break;
		case 14:
			quote = new Faker().princessBride().character();
			break;
		case 15:
			quote = new Faker().rickAndMorty().character();
			break;
		case 16:
			quote = new Faker().starTrek().character();
			break;
		case 17:
			quote = new Faker().twinPeaks().character();
			break;
		case 18:
			quote = new Faker().witcher().character();
			break;
		case 19:
			quote = new Faker().zelda().character();
			break;
		default:
			quote = "";
			break;
		}
		return quote;
	}
}
