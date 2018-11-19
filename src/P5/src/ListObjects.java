/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jonathan
 */
public class ListObjects {
    public int Relevance, Rank;
    public double score;
    public String DocID;
    public ListObjects(int j, String s){
        Relevance = j;
        DocID = s;
    }
    public ListObjects(int j, String s, double d){
        Rank = j;
        DocID = s;
        score = d;
        Relevance = 0;
    }
}
