import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;

class llg {
    public static void main(String[] args) {
        Set<String> words = new HashSet<String>();
        try (InputStreamReader instream = new InputStreamReader(System.in);
                BufferedReader buffer = new BufferedReader(instream)) {
            String line;
            while ((line = buffer.readLine()) != null) {
                words.add(line.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        words.remove("");

        String [] wordlist = words.toArray(new String[0]);
        int [][] graph = GraphBuilder.buildGraph(wordlist);

        for (int k: (new PathSeeker()).longestChain(graph)) {
            System.out.println(wordlist[k]);
        }
    }
}

class GraphBuilder {
    public static int [][] buildGraph(String [] wordlist) {
        long[] wordBitmaps = new long[wordlist.length];
        int wordBitmapsIndex = 0;
        for (String word : wordlist) {
            int first = (int) word.charAt(0);
            int last = (int) word.charAt(word.length() - 1);
            long bitmap = (((long)first) << 32) | (last & 0xffffffffL);
            wordBitmaps[wordBitmapsIndex] = bitmap;
            wordBitmapsIndex++;
        }

        int [] [] graph = new int[wordBitmaps.length] [];
        int i,j;
        int[] curwordlinks = new int[wordBitmaps.length];
        int curwordlinksIndex = 0;
        for (i=0; i < wordBitmaps.length; i++) {
            curwordlinksIndex = 0;
            long left = wordBitmaps[i];
            int leftlast = (int) left;

            for (j=0; j < wordBitmaps.length; j++) {
                if (i == j) continue;
                long right = wordBitmaps[j];
                int rightfirst = (int)(right >> 32);
                if (leftlast == rightfirst) {
                    curwordlinks[curwordlinksIndex] = j;
                    curwordlinksIndex++;
                }
            }
            graph[i] = Arrays.copyOfRange(curwordlinks, 0, curwordlinksIndex);
        }

        return graph;
    }
}

class PathSeeker {
    private boolean [] visited;
    private int [] toppath;
    private int [] stack;

    private void traverseGraph(int [][] graph, int pos, int depth) {
        visited[pos] = true;
        stack[depth - 1] = pos;
        if (depth > toppath.length) {
            toppath = Arrays.copyOfRange(stack,0,depth);
        }
        for (int i: graph[pos]) {
            if (!visited[i]) {
                traverseGraph(graph, i, depth + 1);
            }
        }
        visited[pos] = false;
    }

    public synchronized int[] longestChain(int [][] graph) {
        int gs = graph.length;
        visited = new boolean[gs];
        toppath = new int[0];
        stack = new int[gs];

        for (int i=0; i < gs; i++) {
            traverseGraph(graph, i, 1);
        }
        return toppath;
    }
}
