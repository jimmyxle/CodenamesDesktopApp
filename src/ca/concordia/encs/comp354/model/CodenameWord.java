package ca.concordia.encs.comp354.model;

import java.util.*;
/**
 * Represents a Codename Word. Each word has a clue word which the spy master is going to give as a clue to
 * the operatives to guess. Each clue word has an associated word list, sorted in descending order by "weight" of
 * relation to the clue word, this will be useful in creating a "guessing strategy" for the operatives.
 * The AssociatedWord class is nested within this class.
 * @author Zachary Hynes
 */
public class CodenameWord {
    //============================
    //---------VARIABLES---------
    //============================
    private String clueWord;
    private List<AssociatedWord> associatedWords;


    //============================
    //--------CONSTRUCTORS--------
    //============================
    public CodenameWord(String clueWord, List<AssociatedWord> associatedWords) {
        this.clueWord = clueWord;
        this.associatedWords = Collections.unmodifiableList(new ArrayList<>(associatedWords));
    }


    //============================
    //----------METHODS----------
    //============================
    public String getClueWord() {
        return clueWord;
    }

    public List<AssociatedWord> getAssociatedWords() {
        return associatedWords;
    }

    @Override
    public String toString() {
        return "CodenameWord{" +
                "clueWord='" + clueWord + '\'' +
                ", AssociatedWords=" + associatedWords +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CodenameWord that = (CodenameWord) o;
        return Objects.equals(clueWord, that.clueWord) &&
                Objects.equals(associatedWords, that.associatedWords);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clueWord, associatedWords);
    }

    //============================
    //--------INNER CLASS--------
    //============================
    public static class AssociatedWord {
        //============================
        //---------VARIABLES---------
        //============================
        private String associatedWord;
        private int weight;


        //============================
        //--------CONSTRUCTORS--------
        //============================
        public AssociatedWord(String associatedWord, int weight) {
            this.associatedWord = associatedWord;
            this.weight = weight;
        }


        //============================
        //----------METHODS----------
        //============================
        public String getWord() {
            return associatedWord;
        }

        public int getWeight() {
            return weight;
        }

        @Override
        public String toString() {
            return "AssociatedWord{" +
                    "associatedWord='" + associatedWord + '\'' +
                    ", weight=" + weight +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AssociatedWord that = (AssociatedWord) o;
            return weight == that.weight &&
                    Objects.equals(associatedWord, that.associatedWord);
        }

        @Override
        public int hashCode() {
            return Objects.hash(associatedWord, weight);
        }
    }//END OF AssociatedWord CLASS


    //============================
    //--------INNER CLASS--------
    //============================
    public static class CountFrequencyAssociatedWords {
        //============================
        //---------VARIABLES---------
        //============================
        private String associatedWord;
        private int count;


        //============================
        //--------CONSTRUCTORS--------
        //============================
        public CountFrequencyAssociatedWords(String associatedWord, int count) {
            this.associatedWord = associatedWord;
            this.count = count;
        }


        //============================
        //----------METHODS----------
        //============================
        public String getWord() {
            return associatedWord;
        }
        public int getCount() {
            return count;
        }
        public void setCount(int count) {
            this.count = count;
        }

        @Override
        public String toString() {
            return "CountFrequencyAssociatedWords{" +
                    "associatedWord='" + associatedWord + '\'' +
                    ", count=" + count +
                    '}';
        }

        @Override
        public int hashCode() {
            return Objects.hash(associatedWord, count);
        }
    }//END OF CountFrequencyAssociatedWord CLASS

}//END OF CodenameWord CLASS



