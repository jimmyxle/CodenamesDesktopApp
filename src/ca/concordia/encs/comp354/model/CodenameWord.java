//============================
//----------PACKAGE----------
//============================
package ca.concordia.encs.comp354.model;


//============================
//----------IMPORTS----------
//============================
import java.util.Objects;
import java.util.List;

//============================
//------STATIC VARIABLES------
//============================


//============================
//-----CODENAMEWORD CLASS-----
//============================
public class CodenameWord {
    //============================
    //---------VARIABLES---------
    //============================
    private String clueWord;
    private List<AssociatedWord> AssociatedWords;


    //============================
    //--------CONSTRUCTORS--------
    //============================
    CodenameWord(String clueWord, List<AssociatedWord> associatedWords) {
        this.clueWord = clueWord;
        AssociatedWords = associatedWords;
    }


    //============================
    //----------METHODS----------
    //============================
    //====================
    //-GETTERS & SETTERS-
    //====================
    public String getClueWord() {
        return clueWord;
    }

    public void setClueWord(String clueWord) {
        this.clueWord = clueWord;
    }

    public List<AssociatedWord> getAssociatedWords() {
        return AssociatedWords;
    }

    public void setAssociatedWords(List<AssociatedWord> associatedWords) {
        AssociatedWords = associatedWords;
    }

    //====================
    //-----TO STRING-----
    //====================
    @Override
    public String toString() {
        return "CodenameWord{" +
                "clueWord='" + clueWord + '\'' +
                ", AssociatedWords=" + AssociatedWords +
                '}';
    }


    //====================
    //-------EQUALS-------
    //====================
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CodenameWord that = (CodenameWord) o;
        return Objects.equals(clueWord, that.clueWord) &&
                Objects.equals(AssociatedWords, that.AssociatedWords);
    }

    //====================
    //-------OTHER-------
    //====================


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
        AssociatedWord(String associatedWord, int weight) {
            this.associatedWord = associatedWord;
            this.weight = weight;
        }


        //============================
        //----------METHODS----------
        //============================
        //====================
        //-GETTERS & SETTERS-
        //====================
        public String getAssociatedWord() {
            return associatedWord;
        }

        public void setAssociatedWord(String associatedWord) {
            this.associatedWord = associatedWord;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }


        //====================
        //-----TO STRING-----
        //====================
        @Override
        public String toString() {
            return "AssociatedWord{" +
                    "associatedWord='" + associatedWord + '\'' +
                    ", weight=" + weight +
                    '}';
        }


        //====================
        //-------EQUALS-------
        //====================
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AssociatedWord that = (AssociatedWord) o;
            return weight == that.weight &&
                    Objects.equals(associatedWord, that.associatedWord);
        }


        //====================
        //-------OTHER-------
        //====================


    }//END OF AssociatedWord CLASS


//    public void main(String[] args) {
//        generateRandomCodenameList();
//
//    }//END OF main(String[] args) METHOD
}//END OF CodenameWord CLASS



