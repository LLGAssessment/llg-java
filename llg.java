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

        List<List<Integer>> graph = new ArrayList<List<Integer>>();
        int i,j;
        for (i=0; i < wordlist.length; i++) {
            List<Integer> current = new ArrayList<Integer>();
            graph.add(current);

            String left = wordlist[i];
            char leftlast = left.charAt(left.length() - 1);

            for (j=0; j < wordlist.length; j++) {
                if (i == j) continue;
                String right = wordlist[j];
                if (leftlast == right.charAt(0)) {
                    current.add(new Integer(j));
                }
            }
        }

        for (Integer k: (new PathSeeker()).longestChain(graph)) {
            System.out.println(wordlist[k]);
        }
    }
}

class PathSeeker {
    private boolean [] visited;
    private int [] toppath;
    private int [] stack;

    private void traverseGraph(List<List<Integer>> graph, int pos, int depth) {
        visited[pos] = true;
        stack[depth] = pos;
        if (depth > toppath.length) {
            toppath = Arrays.copyOfRange(stack, 0, depth + 1);
        }
        for (Integer i: graph.get(pos)) {
            if (!visited[i]) {
                traverseGraph(graph, i, depth + 1);
            }
        }
        visited[pos] = false;
    }

    public synchronized int[] longestChain(List<List<Integer>> graph) {
        int gs = graph.size();
        visited = new boolean[gs];
        toppath = new int[0];
        stack = new int[gs];

        for (int i=0; i < gs; i++) {
            traverseGraph(graph, i, 0);
        }
        return toppath;
    }
}
