# python3

# Arguments:
#   * `n` - the number of vertices.
#   * `edges` - list of edges, each edge is a tuple (u, v), 1 <= u, v <= n.
#   * `colors` - list consisting of `n` characters, each belonging to the set {'R', 'G', 'B'}.
# Return value: 
#   * If there exists a proper recoloring, return value is a list containing new colors, similar to the `colors` argument.
#   * Otherwise, return value is None.
def assign_new_colors(n, edges, colors):
    # Insert your code here.
    if n % 3 == 0:
        new_colors = []
        for i in range(n):
            new_colors[i].append("RGB"[i % 3])
        return new_colors
    else:
        return None
    
def main():
    n, m = map(int, input().split())
    colors = input().split()
    edges = []
    for i in range(m):
        u, v = map(int, input().split())
        edges.append((u, v))
    new_colors = assign_new_colors(n, edges, colors)
    if new_colors is None:
        print("Impossible")
    else:
        print(''.join(new_colors))

main()
