/* kmeansyolo.java by Samantha Li */
import java.io.*;
import java.util.Scanner;
// import java.util.Queue;
// import java.util.LinkedList;

public class kmeansyolo
{
   private Centroid[] centroids;
   private final int k;
   private int iterations;
   private BBox[] dataset;
   private final int N;
   private double aIOU;;

   public kmeansyolo(int k, String raw, int N)
   {
      this.k = k;
      centroids = new Centroid[this.k];
      iterations = 0;
      aIOU = Double.NEGATIVE_INFINITY;

      Scanner scan = null;
      File file = new File(raw);
      try { scan = new Scanner(file); }
      catch (java.io.FileNotFoundException e) {
         System.err.println("file not found");
      }
      finally {}
      // Queue<BBox> temp = new LinkedList<BBox>();
      this.N = N;
      dataset = new BBox[N];
      // while (scan.hasNext())
      for (int i = 0; i < N; i++)
      {
         String c = scan.next();
         double w = scan.nextDouble();
         double h = scan.nextDouble();
         BBox newBox = new BBox(c, w, h);
         // temp.add(newBox);
         dataset[i] = newBox;
      }

      // for (int i = 0; i < N; i++)
      // {
      //    dataset[i] = (BBox) temp.remove();
      // }

      int[] random = new int[this.k];
      for (int i = 0; i < this.k; i++)
      {
         random[i] = (int) (Math.random() * N);
         while (true)
         {
            int j;
            for (j = 0; j < i; j++)
            {
               if (random[i] == random[j]) break;
            }
            if (j == i) break;
            random[i] = (int) (Math.random() * N);
         }
         centroids[i] = new Centroid();
         centroids[i].w = dataset[random[i]].w();
         centroids[i].h = dataset[random[i]].h();
         centroids[i].n = 0;
      }

   }

   private class Centroid
   {
      private double w;
      private double h;
      private int n; // number of features in cluster
   }

   public double aIOU() { return aIOU; }

   private double IOU(BBox box, Centroid c)
   {
      double intersection = Math.min(box.w(), c.w) * Math.min(box.h(), c.h);
      return intersection / (box.w() * box.h() + c.w * c.h - intersection);
   }

   private boolean updatePoints()
   {
      boolean converged = true;
      aIOU = 0;
      for (int i = 0; i < N; i++)
      {
         double dist = Double.POSITIVE_INFINITY;
         int cluster = -1;
         for (int j = 0; j < k; j++)
         {
            double iou = IOU(dataset[i], centroids[j]);
            double temp = 1 - iou;
            if (temp < dist)
            {
               dist = temp;
               cluster = j;
               dataset[i].setIOU(iou);
            }
         }
         if (dataset[i].cluster() != cluster) converged = false;
         dataset[i].setCluster(cluster);
         aIOU += dataset[i].IOU();
      }
      aIOU /= N;
      return converged;
   }

   private void updateCentroids()
   {
      for (int i = 0; i < k; i++)
      {
         centroids[i].w = 0;
         centroids[i].h = 0;
         centroids[i].n = 0;
      }

      for (int i = 0; i < N; i++)
      {
         int c = dataset[i].cluster();
         centroids[c].w += dataset[i].w();
         centroids[c].h += dataset[i].h();
         centroids[c].n++;
      }

      for (int i = 0; i < k; i++)
      {
         centroids[i].w /= centroids[i].n;
         centroids[i].h /= centroids[i].n;
      }
   }

   public boolean step()
   {
      boolean converged = updatePoints();
      updateCentroids();
      iterations++;
      return converged;
   }

   public void printStats()
   {
      System.out.println("Iterations: " + iterations);
      System.out.println("k = " + k);
      System.out.println("Centroids: (w, h)");
      for (int i = 0; i < k; i++)
      {
         System.out.printf("%d: %f, %f\n", i, centroids[i].w, centroids[i].h);
      }
      System.out.println("Average IOU = " + aIOU);
      System.out.println("--------------------------------");
   }

   public String printCentroids()
   {
      String c = "";
      int i;
      for (i = 0; i < k-1; i++)
      {
         c = c + String.format("(%f, %f), ", centroids[i].w, centroids[i].h);
      }
      c = c + String.format("(%f, %f)", centroids[i].w, centroids[i].h);
      return c;
   }

   public void converge()
   {
      while (!step());
   }

   public static void main(String[] args)
   {
      if (args.length != 3)
         System.out.println("usage: java kmeansyolo k yourdata.txt numberOfBoxes");

      kmeansyolo test = new kmeansyolo(Integer.parseInt(args[0]), args[1], Integer.parseInt(args[2]));
      Scanner stdin = new Scanner(System.in);
      while (stdin.hasNextLine())
      {
         String token = stdin.next();
         if (token.length() != 1) System.out.println("enter 's' to step, 'c' to continue until convergence, 'p' to print the state of the algorithm, 'q' to exit the process, or '?' for information");
         char c = token.charAt(0);
         if (c == 'q') break;
         else if (c == 'p')
         {
            test.printStats();
            continue;
         }
         else if (c == 's')
         {
            test.step();
         }
         else if (c == 'c')
         {
            test.converge();
            break;
         }
         else if (c == '?') System.out.println("kmeansyolo is a Java class written by Samantha Li to calculate anchor boxes for YOLOv2 (Redmon et al.) using k-means clustering");
         else System.out.println("enter 's' to step, 'c' to continue until convergence, 'p' to print the state of the algorithm, 'q' to exit the process, or '?' for information");
      }

   }

}
