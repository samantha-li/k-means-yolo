public class kmeansyoloIterates
{
   public static void main(String args[])
   {
      if (args.length != 4)
         System.out.println("usage: java kmeansyolo k yourdata.txt numberOfBoxes numberOfTrials");
      int t = Integer.parseInt(args[3]);
      String[] centroids = new String[5];
      double[] aIOUs = new double[5];
      for (int i = 0; i < t; i++)
      {
         kmeansyolo trial = new kmeansyolo(Integer.parseInt(args[0]), args[1], Integer.parseInt(args[2]));
         trial.converge();
         double temp = trial.aIOU();
         int index = -1;
         for (int j = 0; j < 5; j++)
         {
            if (aIOUs[j] < temp) index = j;
         }
         if (index != -1)
         {
            aIOUs[index] = temp;
            centroids[index] = trial.printCentroids();
         }
      }
      for (int i = 0; i < 5; i++)
      {
         System.out.printf("Centroids: %s\n aIOU: %f\n\n", centroids[i], aIOUs[i]);
      }

   }
}
