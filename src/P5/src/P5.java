/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @author Jonathan
 */
public class P5 {
    public static void main(String[] args) throws FileNotFoundException, IOException{
        List<String> QIDS = new ArrayList();
        File qrels = new File("C:\\Users\\Jonathan\\Documents\\NetBeansProjects\\P5\\src\\qrels");
        BufferedReader qrelsbr = new BufferedReader(new FileReader(qrels));
        String temp;
        Map<String, List<ListObjects>> QID = new HashMap<>();
        while((temp = qrelsbr.readLine()) != null){
            String QueryID = temp.substring(0, temp.indexOf(" "));
            temp = temp.substring(temp.indexOf(" ")+3, temp.length());
            String DocID = temp.substring(0, temp.indexOf(" "));
            int rel = Integer.parseInt(temp.substring(temp.indexOf(" ")+1, temp.length()));
            if(QID.containsKey(QueryID)){
                ListObjects l = new ListObjects(rel, DocID);
                QID.get(QueryID).add(l);
            }
            else{
                List<ListObjects> QIDlist = new ArrayList();
                QIDlist.add(new ListObjects(rel, DocID));
                QID.put(QueryID, QIDlist);
                QIDS.add(QueryID);
            }
        }
        
        Map<String, List<ListObjects>> bm25QID = new HashMap<>();
        File bm25 = new File("C:\\Users\\Jonathan\\Documents\\NetBeansProjects\\P5\\src\\bm25.trecrun");
        BufferedReader bm25br = new BufferedReader(new FileReader(bm25));
        while((temp = bm25br.readLine()) != null){
            String QueryID = temp.substring(0, temp.indexOf(" "));
            temp = temp.substring(9, temp.length());
            String DocID = temp.substring(0, temp.indexOf(" "));
            temp = temp.substring(temp.indexOf(" ")+1, temp.length());
            int rank = Integer.parseInt(temp.substring(0, temp.indexOf(" ")));
            temp = temp.substring(temp.indexOf(" ")+1, temp.length());
            double score = Double.parseDouble(temp.substring(0, temp.indexOf(" ")));
            if(bm25QID.containsKey(QueryID)){
                ListObjects l = new ListObjects(rank, DocID, score);
                bm25QID.get(QueryID).add(l);
            }
            else{
                List<ListObjects> bm25list = new ArrayList();
                bm25list.add(new ListObjects(rank, DocID, score));
                bm25QID.put(QueryID, bm25list);
            }
        }
        
        double bm25NDCG = 0.0, bm25MRR = 0.0, bm25P5 = 0.0, bm25P10 = 0.0, bm25R10 = 0.0, bm25F1 = 0.0, bm25AP = 0.0;
        for(int i = 0; i < QIDS.size(); i++){
            bm25NDCG += NDCGatK(QID.get(QIDS.get(i)), bm25QID.get(QIDS.get(i)), 15);
            bm25MRR += MRR(QID.get(QIDS.get(i)), bm25QID.get(QIDS.get(i)));
            bm25P5 += PatK(QID.get(QIDS.get(i)), bm25QID.get(QIDS.get(i)), 5);
            bm25P10 += PatK(QID.get(QIDS.get(i)), bm25QID.get(QIDS.get(i)), 10);
            bm25R10 += RecallatK(QID.get(QIDS.get(i)), bm25QID.get(QIDS.get(i)), 10);
            bm25F1 += F1atK(QID.get(QIDS.get(i)), bm25QID.get(QIDS.get(i)), 10);
            bm25AP += AP(QID.get(QIDS.get(i)), bm25QID.get(QIDS.get(i)));
        }
        System.out.println("\n\nbm25.trecrun");
        System.out.println("bm25.trecrun NDCG@15 " + (bm25NDCG / (double) QIDS.size()));
        System.out.println("bm25.trecrun MRR " + (bm25MRR / (double) QIDS.size()));
        System.out.println("bm25.trecrun P@5 " + (bm25P5 / (double) QIDS.size()));
        System.out.println("bm25.trecrun P@10 " + (bm25P10 / (double) QIDS.size()));
        System.out.println("bm25.trecrun Recall@10 " + (bm25R10 / (double) QIDS.size()));
        System.out.println("bm25.trecrun F1@10 " + (bm25F1 / (double) QIDS.size()));
        System.out.println("bm25.trecrun AP " + (bm25AP / (double) QIDS.size()));
        
        Map<String, List<ListObjects>> qlQID = new HashMap<>();
        File ql = new File("C:\\Users\\Jonathan\\Documents\\NetBeansProjects\\P5\\src\\ql.trecrun");
        BufferedReader qlbr = new BufferedReader(new FileReader(ql));
        while((temp = qlbr.readLine()) != null){
            String QueryID = temp.substring(0, temp.indexOf(" "));
            temp = temp.substring(9, temp.length());
            String DocID = temp.substring(0, temp.indexOf(" "));
            temp = temp.substring(temp.indexOf(" ")+1, temp.length());
            int rank = Integer.parseInt(temp.substring(0, temp.indexOf(" ")));
            temp = temp.substring(temp.indexOf(" ")+1, temp.length());
            double score = Double.parseDouble(temp.substring(0, temp.indexOf(" ")));
            if(qlQID.containsKey(QueryID)){
                ListObjects l = new ListObjects(rank, DocID, score);
                qlQID.get(QueryID).add(l);
            }
            else{
                List<ListObjects> qllist = new ArrayList();
                qllist.add(new ListObjects(rank, DocID, score));
                qlQID.put(QueryID, qllist);
            }
        }
        
        double qlNDCG = 0.0, qlMRR = 0.0, qlP5 = 0.0, qlP10 = 0.0, qlR10 = 0.0, qlF1 = 0.0, qlAP = 0.0;
        for(int i = 0; i < QIDS.size(); i++){
            qlNDCG += NDCGatK(QID.get(QIDS.get(i)), qlQID.get(QIDS.get(i)), 15);
            qlMRR += MRR(QID.get(QIDS.get(i)), qlQID.get(QIDS.get(i)));
            qlP5 += PatK(QID.get(QIDS.get(i)), qlQID.get(QIDS.get(i)), 5);
            qlP10 += PatK(QID.get(QIDS.get(i)), qlQID.get(QIDS.get(i)), 10);
            qlR10 += RecallatK(QID.get(QIDS.get(i)), qlQID.get(QIDS.get(i)), 10);
            qlF1 += F1atK(QID.get(QIDS.get(i)), qlQID.get(QIDS.get(i)), 10);
            qlAP += AP(QID.get(QIDS.get(i)), qlQID.get(QIDS.get(i)));
        }
        System.out.println("\n\nql.trecrun");
        System.out.println("ql.trecrun NDCG@15 " + (qlNDCG / (double) QIDS.size()));
        System.out.println("ql.trecrun MRR " + (qlMRR / (double) QIDS.size()));
        System.out.println("ql.trecrun P@5 " + (qlP5 / (double) QIDS.size()));
        System.out.println("ql.trecrun P@10 " + (qlP10 / (double) QIDS.size()));
        System.out.println("ql.trecrun Recall@10 " + (qlR10 / (double) QIDS.size()));
        System.out.println("ql.trecrun F1@10 " + (qlF1 / (double) QIDS.size()));
        System.out.println("ql.trecrun AP " + (qlAP / (double) QIDS.size()));
        
        Map<String, List<ListObjects>> sdmQID = new HashMap<>();
        File sdm = new File("C:\\Users\\Jonathan\\Documents\\NetBeansProjects\\P5\\src\\sdm.trecrun");
        BufferedReader sdmbr = new BufferedReader(new FileReader(sdm));
        while((temp = sdmbr.readLine()) != null){
            String QueryID = temp.substring(0, temp.indexOf(" "));
            temp = temp.substring(9, temp.length());
            String DocID = temp.substring(0, temp.indexOf(" "));
            temp = temp.substring(temp.indexOf(" ")+1, temp.length());
            int rank = Integer.parseInt(temp.substring(0, temp.indexOf(" ")));
            temp = temp.substring(temp.indexOf(" ")+1, temp.length());
            double score = Double.parseDouble(temp.substring(0, temp.indexOf(" ")));
            if(sdmQID.containsKey(QueryID)){
                ListObjects l = new ListObjects(rank, DocID, score);
                sdmQID.get(QueryID).add(l);
            }
            else{
                List<ListObjects> sdmlist = new ArrayList();
                sdmlist.add(new ListObjects(rank, DocID, score));
                sdmQID.put(QueryID, sdmlist);
            }
        }
        
        double sdmNDCG = 0.0, sdmMRR = 0.0, sdmP5 = 0.0, sdmP10 = 0.0, sdmR10 = 0.0, sdmF1 = 0.0, sdmAP = 0.0;
        for(int i = 0; i < QIDS.size(); i++){
            sdmNDCG += NDCGatK(QID.get(QIDS.get(i)), sdmQID.get(QIDS.get(i)), 15);
            sdmMRR += MRR(QID.get(QIDS.get(i)), sdmQID.get(QIDS.get(i)));
            sdmP5 += PatK(QID.get(QIDS.get(i)), sdmQID.get(QIDS.get(i)), 5);
            sdmP10 += PatK(QID.get(QIDS.get(i)), sdmQID.get(QIDS.get(i)), 10);
            sdmR10 += RecallatK(QID.get(QIDS.get(i)), sdmQID.get(QIDS.get(i)), 10);
            sdmF1 += F1atK(QID.get(QIDS.get(i)), sdmQID.get(QIDS.get(i)), 10);
            sdmAP += AP(QID.get(QIDS.get(i)), sdmQID.get(QIDS.get(i)));
        }
        System.out.println("\n\nsdm.trecrun");
        System.out.println("sdm.trecrun NDCG@15 " + (sdmNDCG / (double) QIDS.size()));
        System.out.println("sdm.trecrun MRR " + (sdmMRR / (double) QIDS.size()));
        System.out.println("sdm.trecrun P@5 " + (sdmP5 / (double) QIDS.size()));
        System.out.println("sdm.trecrun P@10 " + (sdmP10 / (double) QIDS.size()));
        System.out.println("sdm.trecrun Recall@10 " + (sdmR10 / (double) QIDS.size()));
        System.out.println("sdm.trecrun F1@10 " + (sdmF1 / (double) QIDS.size()));
        System.out.println("sdm.trecrun AP " + (sdmAP / (double) QIDS.size()));
        
        Map<String, List<ListObjects>> stressQID = new HashMap<>();
        File stress = new File("C:\\Users\\Jonathan\\Documents\\NetBeansProjects\\P5\\src\\stress.trecrun");
        BufferedReader stressbr = new BufferedReader(new FileReader(stress));
        List<String> stressQIDS = new ArrayList();
        while((temp = stressbr.readLine()) != null){
            String QueryID = temp.substring(0, temp.indexOf(" "));
            temp = temp.substring(9, temp.length());
            String DocID = temp.substring(0, temp.indexOf(" "));
            temp = temp.substring(temp.indexOf(" ")+1, temp.length());
            int rank = Integer.parseInt(temp.substring(0, temp.indexOf(" ")));
            temp = temp.substring(temp.indexOf(" ")+1, temp.length());
            double score = Double.parseDouble(temp.substring(0, temp.indexOf(" ")));
            if(stressQID.containsKey(QueryID)){
                ListObjects l = new ListObjects(rank, DocID, score);
                stressQID.get(QueryID).add(l);
            }
            else{
                List<ListObjects> stresslist = new ArrayList();
                stresslist.add(new ListObjects(rank, DocID, score));
                stressQID.put(QueryID, stresslist);
                stressQIDS.add(QueryID);
            }
        }
        
        double stressNDCG = 0.0, stressMRR = 0.0, stressP5 = 0.0, stressP10 = 0.0, stressR10 = 0.0, stressF1 = 0.0, stressAP = 0.0;
        for(int i = 0; i < stressQIDS.size(); i++){
            stressNDCG += NDCGatK(QID.get(stressQIDS.get(i)), stressQID.get(stressQIDS.get(i)), 15);
            stressMRR += MRR(QID.get(stressQIDS.get(i)), stressQID.get(stressQIDS.get(i)));
            stressP5 += PatK(QID.get(stressQIDS.get(i)), stressQID.get(stressQIDS.get(i)), 5);
            stressP10 += PatK(QID.get(stressQIDS.get(i)), stressQID.get(stressQIDS.get(i)), 10);
            stressR10 += RecallatK(QID.get(stressQIDS.get(i)), stressQID.get(stressQIDS.get(i)), 10);
            stressF1 += F1atK(QID.get(stressQIDS.get(i)), stressQID.get(stressQIDS.get(i)), 10);
            stressAP += AP(QID.get(stressQIDS.get(i)), stressQID.get(stressQIDS.get(i)));
        }
        System.out.println("\n\nstress.trecrun");
        System.out.println("stress.trecrun NDCG@15 " + (stressNDCG / (double) stressQIDS.size()));
        System.out.println("stress.trecrun MRR " + (stressMRR / (double) stressQIDS.size()));
        System.out.println("stress.trecrun P@5 " + (stressP5 / (double) stressQIDS.size()));
        System.out.println("stress.trecrun P@10 " + (stressP10 / (double) stressQIDS.size()));
        System.out.println("stress.trecrun Recall@10 " + (stressR10 / (double) stressQIDS.size()));
        System.out.println("stress.trecrun F1@10 " + (stressF1 / (double) stressQIDS.size()));
        System.out.println("stress.trecrun AP " + (stressAP / (double) stressQIDS.size()));
        
        
    }
    public static double NDCGatK(List<ListObjects> R, List<ListObjects> L, int K){
        List<ListObjects> L2 = L;
        for (int i = 0; i < L2.size(); i++){
            for(int j = 0; j < R.size(); j++){
                if (L2.get(i).DocID.equals(R.get(j).DocID)){
                    L2.get(i).Relevance = R.get(j).Relevance;
                }
            }
        }
        boolean t = true;
        while(t){
            t = false;
            for (int i = 0; i < L2.size()-1; i++){
                if (L2.get(i).Relevance < L2.get(i+1).Relevance){
                    ListObjects temp = L2.get(i);
                    L2.set(i, L2.get(i+1));
                    L2.set(i+1, temp);
                    t = true;
                }
            }
        }
        List<Double> d = new ArrayList();
        d.add((double)L2.get(0).Relevance);
        for (int i = 1; i < L2.size(); i++){
            d.add(d.get(i-1) + (L2.get(i).Relevance / (Math.log(i+1)/Math.log(2))));
        }
        List<Double> d2 = new ArrayList();
        d2.add((double)L.get(0).Relevance);
        for (int i = 1; i < L.size(); i++){
            d2.add(d2.get(i-1) + (L.get(i).Relevance / (Math.log(i+1)/Math.log(2))));
        }
        if(d.size() > K){
            if(d2.get(K) == 0){
                return 0.0;
            }
            return d.get(K)/d2.get(K);
        }
        else{
            return 0.0;
        }
    }
    
    public static double MRR(List<ListObjects> R, List<ListObjects> L){
        for (int i = 0; i < L.size(); i++){
            for(int j = 0; j < R.size(); j++){
                if (L.get(i).DocID.equals(R.get(j).DocID) && R.get(j).Relevance > 0){
                    return 1.0 / (double) (i+1);
                }
            }
        }
        return 0.0;
    }
    
    public static double PatK(List<ListObjects> R, List<ListObjects> L, int K){
        int count = 0;
        for (int i = 0; i < L.size(); i++){
            for(int j = 0; j < R.size(); j++){
                if (L.get(i).DocID.equals(R.get(j).DocID) && R.get(j).Relevance > 0){
                    if(i < K)
                        count++;
                }
            }
        }
        return (double) count / (double) K;
    }
    
    public static double RecallatK(List<ListObjects> R, List<ListObjects> L, int K){
        int count = 0;
        int count2 = 0;
        for (int i = 0; i < L.size(); i++){
            for(int j = 0; j < R.size(); j++){
                if (L.get(i).DocID.equals(R.get(j).DocID) && R.get(j).Relevance > 0){
                    count++;
                    if (i < K){
                        count2++;
                    }
                }
            }
        }
        if (count == 0){
            return 0.0;
        }
        return (double) count2 / (double) count;
    }
    
    public static double F1atK(List<ListObjects> R, List<ListObjects> L, int K){
        double p = PatK(R, L, K);
        double r = RecallatK(R, L, K);
        if (r == 0 || p == 0){
            return 0.0;
        }
        return (2*r*p)/(r+p);
    }
    
    public static double AP(List<ListObjects> R, List<ListObjects> L){
        double sum = 0.0;
        int count = 0;
        for (int i = 0; i < L.size(); i++){
            for(int j = 0; j < R.size(); j++){
                if (L.get(i).DocID.equals(R.get(j).DocID) && R.get(j).Relevance > 0){
                    count++;
                    sum += (double)count / (double)(i+1);
                }
            }
        }
        if (count == 0){
            return 0.0;
        }
        return sum / (double)count;
    }
}
