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
        words = null;
        Arrays.sort(wordlist);

        int [] [] graph = new int[wordlist.length] [];
        int i,j;
        List<Integer> curwordlinks = new ArrayList<Integer>();
        for (i=0; i < wordlist.length; i++) {
            curwordlinks.clear();
            String left = wordlist[i];
            char leftlast = left.charAt(left.length() - 1);

            for (j=0; j < wordlist.length; j++) {
                if (i == j) continue;
                String right = wordlist[j];
                if (leftlast == right.charAt(0)) {
                    curwordlinks.add(new Integer(j));
                }
            }
            int s = curwordlinks.size();
            graph[i] = new int [s];
            for (j=0; j < s; j++) {
                graph[i][j] = curwordlinks.get(j);
            }
        }

        for (int k: (new PathSeeker()).longestChain(graph)) {
            System.out.println(wordlist[k]);
        }
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
            toppath = Arrays.copyOfRange(stack, 0, depth);
        }
        for (Integer i: graph[pos]) {
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
