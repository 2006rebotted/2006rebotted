package rs2.game.model.entity.mob.player;

/**
 * Represents a {@link Player}s appearance.
 * 
 * @author Vult-R
 */
public final class Appearance {

	/**
	 * Represents the a gender model of a {@link Player}
	 */
      public enum Gender {

            MALE(0),

            FEMALE(1);

            private int code;

            private Gender(final int id) {
                  this.code = id;
            }

            public int getCode() {
                  return code;
            }

      }

      /**
       * The gender of this player.
       */
      private final Gender gender;

      /**
       * The head model
       */
      private final int head;

      /**
       * The jaw model
       */
      private final int jaw;

      /**
       * The torso model
       */
      private final int torso;

      /**
       * The arms model
       */
      private final int arms;

      /**
       * The hands model
       */
      private final int hands;

      /**
       * The legs model
       */
      private final int legs;

      /**
       * The feet model
       */
      private final int feet;

      /**
       * The color of the hair model
       */
      private final int hairColor;

      /**
       * The color of the torso model
       */
      private final int torsoColor;

      /**
       * The color of the legs model
       */
      private final int legsColor;

      /**
       * The color of the feet model
       */
      private final int feetColor;

      /**
       * The color of skin
       */
      private final int skinColor;

      public Appearance(final Gender gender, final int head, final int jaw, final int torso,
                  final int arms, final int hands, final int legs, final int feet, int hairColor,
                  int torsoColor, int legsColor, int feetColor, int skinColor) {
            this.gender = gender;
            this.head = head;
            this.jaw = jaw;
            this.torso = torso;
            this.arms = arms;
            this.hands = hands;
            this.legs = legs;
            this.feet = feet;
            this.hairColor = hairColor;
            this.torsoColor = torsoColor;
            this.legsColor = legsColor;
            this.feetColor = feetColor;
            this.skinColor = skinColor;
      }

      public int getArms() {
            return arms;
      }

      public Appearance getDefaultAppearance() {
            return Player.DEFAULT_APPEARANCE;
      }

      public int getFeet() {
            return feet;
      }

      public Gender getGender() {
            return gender;
      }

      public int getHands() {
            return hands;
      }

      public int getHead() {
            return head;
      }

      public int getJaw() {
            return jaw;
      }

      public int getLegs() {
            return legs;
      }

      public int getTorso() {
            return torso;
      }

      public int getFeetColor() {
            return feetColor;
      }

      public int getHairColor() {
            return hairColor;
      }

      public int getLegsColor() {
            return legsColor;
      }

      public int getSkinColor() {
            return skinColor;
      }

      public int getTorsoColor() {
            return torsoColor;
      }

      @Override
      public String toString() {
            return "Gender: " + getGender().name() + " Head: " + getHead() + " Jaw: " + getJaw()
                        + " Torso: " + getTorso() + " Arms: " + getArms() + " Hands: " + getHands()
                        + " Legs: " + getLegs() + " feet: " + getFeet();
      }

}
