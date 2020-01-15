package algorithms;


import dataStructure.*;
import gameClient.Fruits;
import gameClient.Players;
import utils.Point3D;
import utils.StdDraw;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameAlgo {

    private final double  EPS = 0.000000000000001;

    public  edge_data checkWhereTheFruit(Fruits p)
    {
        node_data dest  = new NodeData(0,0,0);
        node_data src = new NodeData(0,0,0);
        edge_data ans = new EdgeData(0,0,0);
        Point3D theLocOfTHEFRUIT = p.getLocation();
        Iterator<node_data> iterV =  StdDraw.theMain.fullGame.getGraphM().getV().iterator();
        while(iterV.hasNext())
        {

            Iterator<edge_data> iterE = StdDraw.theMain.fullGame.getGraphM().getE(iterV.next().getKey()).iterator();
            while(iterE.hasNext())
            {
                edge_data temp = iterE.next();
                src = StdDraw.theMain.fullGame.getGraphM().getNode(temp.getSrc());
                dest = StdDraw.theMain.fullGame.getGraphM().getNode(temp.getDest());
                double disSD = returnDis(dest.getLocation().x(),src.getLocation().x(),dest.getLocation().y(),src.getLocation().y());
                double disSF = returnDis(p.getLocation().x(),src.getLocation().x(),p.getLocation().y(),src.getLocation().y());
                double disDF = returnDis(p.getLocation().x(),dest.getLocation().x(),p.getLocation().y(),dest.getLocation().y());
                if(((disDF+disSF)-disSD)<=0.000000000000001) {ans =temp ;}
            }
        }
        return ans ;
    }

    private  double returnDis(double x1, double x2, double y1, double y2)
    {
        double x = Math.pow((x2-x1),2);
        double y = Math.pow((y2-y1),2);
        return Math.sqrt(x+y);
    }

    public String findTheNearestBanana(Players p)
    {
        Graph_Algo newAlgo = new Graph_Algo();
        StdDraw.theMain.fullGame.setAlgo(newAlgo);
        StdDraw.theMain.fullGame.getAlgo().init((StdDraw.theMain.fullGame.getGraphM()));
        ArrayList<Fruits> FruList = StdDraw.theMain.fullGame.getF();
        double min = Double.MAX_VALUE;
        double goToDest = -1 ;
        double NextStep = -1 ;
        for(Fruits f : FruList)
        {

            if(f.getType() == -1)
            {
                int dest = checkWhereTheFruit(f).getSrc();
                int Next = checkWhereTheFruit(f).getDest();
                if (StdDraw.theMain.fullGame.getAlgo().shortestPathDist(p.getSrc() , dest) < min)
                {
                    min = StdDraw.theMain.fullGame.getAlgo().shortestPathDist(p.getSrc() , dest) ;
                    goToDest = dest ;
                    NextStep = Next;

                }
            }
            if(f.getType() == 1)
            {
                int dest = checkWhereTheFruit(f).getDest();
                int Next = checkWhereTheFruit(f).getSrc();
                if (StdDraw.theMain.fullGame.getAlgo().shortestPathDist(p.getSrc() , dest) < min)
                {
                    min = StdDraw.theMain.fullGame.getAlgo().shortestPathDist(p.getSrc() , dest) ;
                    goToDest = dest ;
                    NextStep = Next;

                }
            }
        }
        return "" + goToDest + "," +NextStep ;
    }






    public int ReturnTheNextID(Players p)
    {
        Graph_Algo newAlgo = new Graph_Algo();
        StdDraw.theMain.fullGame.setAlgo(newAlgo);
        StdDraw.theMain.fullGame.getAlgo().init((StdDraw.theMain.fullGame.getGraphM()));
        ArrayList<Integer> TheAns = new ArrayList<>();
        String temp = findTheNearestBanana(p);
        String[] tempARR = temp.split(",");
        List<node_data> TheNodesList = StdDraw.theMain.fullGame.getAlgo().shortestPath(p.getSrc(), (int) Double.parseDouble(tempARR[1]));
        for(node_data node : TheNodesList)
        {
            TheAns.add(node.getKey());
        }
        TheAns.add((int) Double.parseDouble(tempARR[0]));
        if(TheAns.size()==1){return TheAns.get(0);}
        return TheAns.get(1);
    }



    public void NavigateAUTO(Players p) {
        if (p.getDest() == -1)
        {
            int theWay = ReturnTheNextID(p);
            StdDraw.theMain.fullGame.getGame().chooseNextEdge(p.getKey(),theWay);
        }
    }

}