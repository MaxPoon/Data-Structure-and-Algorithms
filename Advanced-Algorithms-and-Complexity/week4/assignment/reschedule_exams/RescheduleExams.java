import java.util.Collection;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.File;

public class RescheduleExams {

    class Edge {
        int u, v;
        public Edge(int u, int v) {
            this.u = u;
            this.v = v;
        }
    }

    char[] assignNewColors(int n, Edge[] edges, char[] colors) {
        // Insert your code here.
        if (n % 3 == 0) {
            char[] newColors = colors.clone();
            for (int i = 0; i < n; i++) {
                newColors[i] = "RGB".charAt(i % 3);
            }
            return newColors;
        } else {
            return null;
        }
    } 

    void run() {
        Scanner scanner = new Scanner(System.in);
        PrintWriter writer = new PrintWriter(System.out);

        int n = scanner.nextInt();
        int m = scanner.nextInt();
        scanner.nextLine();
        
        String colorsLine = scanner.nextLine();
        char[] colors = colorsLine.toCharArray();

        Edge[] edges = new Edge[m];
        for (int i = 0; i < m; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            edges[i] = new Edge(u, v);
        }

        char[] newColors = assignNewColors(n, edges, colors);

        if (newColors == null) {
            writer.println("Impossible");
        } else {
            writer.println(new String(newColors));
        }

        writer.close();
    }

    public static void main(String[] args) throws FileNotFoundException {
        new RescheduleExams().run();
    }
}
