import swiftbot.*;
import java.util.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.ThreadLocalRandom;

public class swiftbot_simon {
	
	static SwiftBotAPI swiftBot;
	public static void main(String[] args) {
		int score = 0;
		System.out.println("red=B");
		System.out.println("blue=Y");
		System.out.println("green=A");
		System.out.println("yellow=X");
		System.out.println("U only have 18 second 2 press buttons");
		try {
			swiftBot = SwiftBotAPI.INSTANCE;
			while (true) {
				List<String> colorHistory = testIndividualUnderlights();
				List<String> colorHistory_2 = PlayerPlays();
				if (colorHistory == colorHistory_2) {
					score+=1;
				}
				else {
					if (score <= 5) {
						GameOver();
					}
					else {
						System.out.println("Your Brain is a failure just like you.");
					}
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("\nI2C disabled!");
			System.exit(5);
		}
	}
	
	//We will be using testIndividualUnderlights() and testing buttons from DoesMySwiftBotWork
	//red=Underlight.BACK_LEFT=B blue=Underlight.BACK_RIGHT=Y
	//green=Underlight.FRONT_LEFT=A yellow=Underlight.FRONT_RIGHT=X
	public static List<String> testIndividualUnderlights() throws InterruptedException {
		// Declaring three variables containing the RGB values for red, green and blue.
		int[] red = new int[] { 255, 0, 0 };
		int[] blue = new int[] { 0, 0, 255 };
		int[] green = new int[] { 0, 255, 0 };
		int[] yellow = new int[] {255,255,0}; //yellow=red+green
		List<String> colorHistory = new ArrayList<>();
		try {
			Underlight[] underlights = new Underlight[] {Underlight.BACK_LEFT, Underlight.BACK_RIGHT,Underlight.FRONT_LEFT, Underlight.FRONT_RIGHT};
			Underlight randomLight = underlights[ThreadLocalRandom.current().nextInt(underlights.length)];
			System.out.println("Random Underlight: " + randomLight);
			if (randomLight == Underlight.BACK_LEFT) {
				swiftBot.setUnderlight(Underlight.BACK_LEFT, red);
				Thread.sleep(200);
				colorHistory.add("red");
				//B
			}
			else if (randomLight == Underlight.BACK_RIGHT) {
				swiftBot.setUnderlight(Underlight.BACK_RIGHT, blue);
				Thread.sleep(200);
				colorHistory.add("blue");
				//Y
			}
			else if (randomLight == Underlight.FRONT_LEFT) {
				swiftBot.setUnderlight(Underlight.FRONT_LEFT, green);
				Thread.sleep(200);
				colorHistory.add("green");
				//A
			}
			else if (randomLight == Underlight.FRONT_RIGHT) {
				swiftBot.setUnderlight(Underlight.FRONT_RIGHT, yellow);
				Thread.sleep(200);
				colorHistory.add("yellow");
				//X
			}
			System.out.println("Colors shown so far: " + colorHistory);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR: Unable to set under light");
			System.exit(5);
		}
		return colorHistory;
	}
	//In order to make it like player plays using pressing button in the swiftbot we will use testButton from DoesMySwiftBotWork.java
	public static List<String> PlayerPlays() throws InterruptedException {
		/*Scanner myObj = new Scanner(System.in);
		System.out.println("Enter The Sequence");
		String Sequence = myObj.nextLine();*/
		List<String> colorHistory_2 = new ArrayList<>();
		try {
			long endtime = System.currentTimeMillis() + 18_000;
			swiftBot.enableButton(Button.A, () -> {
				colorHistory_2.add("green");	//green
				swiftBot.disableButton(Button.A);
			});
			swiftBot.enableButton(Button.B, () -> {
				colorHistory_2.add("red");	//red
				swiftBot.disableButton(Button.B);
			});
			swiftBot.enableButton(Button.X, () -> {
				colorHistory_2.add("yellow");	//yellow
				swiftBot.disableButton(Button.X);
			});
			swiftBot.enableButton(Button.Y, () -> {
				colorHistory_2.add("blue");	//blue
				swiftBot.disableButton(Button.Y);
			});
			while (System.currentTimeMillis() < endtime) {
				; // This while loop does nothing for 10 seconds.
			}
			swiftBot.disableAllButtons(); // Turns off all buttons now that it's been 10 seconds.
			System.out.println("All buttons are now off.");

		} catch (Exception e) {
			System.out.println("ERROR occurred when setting up buttons.");
			e.printStackTrace();
			System.exit(5);
		}
		return colorHistory_2;
	}
	//SwiftBot should move in a v-shape (45 degrees) and use testIndividualUnderlights() 2 get all show random lights
	public static void GameOver() throws InterruptedException {
		try {
			// Moves the wheels with the set left wheel and right wheel velocity, for 3
			// seconds.
			swiftBot.move(10, 10, 3000);
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("ERROR: Error while testing wheel");
			System.exit(5);
		}
		int[] red = new int[] { 255, 0, 0 };
		int[] blue = new int[] { 0, 0, 255 };
		int[] green = new int[] { 0, 255, 0 };
		int[] yellow = new int[] {255,255,0};
		try {
			Underlight[] underlights = new Underlight[] {
		            Underlight.BACK_LEFT, 
		            Underlight.BACK_RIGHT,
		            Underlight.FRONT_LEFT, 
		            Underlight.FRONT_RIGHT
		        };
			List<Underlight> underlightList = Arrays.asList(underlights);
	        Collections.shuffle(underlightList);

	        // Iterate over all in random order
	        for (Underlight u : underlightList) {
	        	if (u == Underlight.BACK_LEFT) {
					swiftBot.setUnderlight(Underlight.BACK_LEFT, red);
					Thread.sleep(200);
					//B
				}
	        	else if (u == Underlight.BACK_RIGHT) {
					swiftBot.setUnderlight(Underlight.BACK_RIGHT, blue);
					Thread.sleep(200);
					//Y
				}
	        	else if (u == Underlight.FRONT_LEFT) {
					swiftBot.setUnderlight(Underlight.FRONT_LEFT, green);
					Thread.sleep(200);
					//A
				}
	        	else if (u == Underlight.FRONT_RIGHT) {
					swiftBot.setUnderlight(Underlight.FRONT_RIGHT, yellow);
					Thread.sleep(200);
					//X
				}
	        }
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR: Error while testing wheel");
			System.exit(5);
		}
	}
}
