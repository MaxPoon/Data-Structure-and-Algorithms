# python3
class NumberTypeclass:
	def zero(self): return 0
	def one(self): return 1
	def positive(self,x): return x > 0
	def iszero(self,x): return x == 0
	def nonnegative(self,x): return self.positive(x) or self.iszero(x)
	def coerce(self, x): return x
	def coerce_vec(self, x): return [self.coerce(xi) for xi in x]
	def coerce_mtx(self, x): return [self.coerce_vec(xi) for xi in x]
	

class RealFiniteTolerance(NumberTypeclass):
	def __init__(self, eps=1e-6):
		super(RealFiniteTolerance, self).__init__()
		self.eps = eps
		assert eps >= 0
		
	def zero(self): return 0.0
	def one(self): return 1.0
	def iszero(self,x): return abs(x) < self.eps
	def coerce(self, x): return float(x)

def _subtract_scaled_row(row1, row2, k, numclass):
	"""row1 -= k*row2"""
	if numclass.iszero(k): return
	for i, row2_i in enumerate(row2):
		row1[i] -= k*row2_i

RESOLUTION_NO = "no"
RESOLUTION_SOLVED = "solved"
RESOLUTION_UNBOUNDED = "unbounded"
RESOLUTION_INCOMPATIBLE = "incompatible"

		
class SimplexSolver:
	def __init__(self, a, b, c, basis, numclass, clean_c_row=False ):
		assert len(a) == len(b) #number of rows of A must be the same as number of elements of b
		for aj in a:
			assert len(aj) == len(c) #each row of A must have the same len as c
			
		self.numclass = numclass
		self.a = a
		self.b = b
		self.c = c
		self.basis = basis
		self.n = len(c)
		self.m = len(b)
		self.resolution = RESOLUTION_NO
		if clean_c_row: self._diagonalize_c_row()
		self._validate_diagonzlized()
		
	def _diagonalize_c_row(self):
		#diagonalize it (it has nonzero values now)
		c = self.c
		for j, i in enumerate(self.basis):
			if not self.numclass.iszero(c[i]):
				_subtract_scaled_row( c, self.a[j], c[i], self.numclass)
				assert self.numclass.iszero( c[i] )
				c[i] = self.numclass.zero()

		
	def vertex(self):
		v = [self.numclass.zero()] * self.n
		for i ,val in zip(self.basis, self.b):
			v[i] = val
		return v
	def _validate_diagonzlized(self):
		for i in self.basis:
			assert self.numclass.iszero(self.c[i])

		for j, a_j in enumerate(self.a):
			for j1, i in enumerate(self.basis):
				if j1 == j:
					assert self.numclass.iszero( a_j[i] - self.numclass.one() )
				else:
					if not self.numclass.iszero( a_j[i] ): raise AssertionError("A: column {i} row {j} must be 0".format(**locals()))
		for bi in self.b:
			assert self.numclass.nonnegative(bi)

	def _find_leading_column(self):
		imin = min(range(self.n), key=lambda i: self.c[i])
		if self.numclass.nonnegative(self.c[imin]):
			return None
		else:
			return imin
						
	def step(self):
		#determine leading column
		#index of a minimal value
		i_lead = self._find_leading_column()

		if i_lead is None:
			self.resolution = RESOLUTION_SOLVED
			return False

		assert i_lead not in self.basis

		#now find the leading row
		best_ratio = None
		best_row = None

		for j, b_j in enumerate(self.b):
			a_ji = self.a[j][i_lead]

			#treat special case when A contains zeros
			if self.numclass.iszero(a_ji):
				continue

			#zeros in B are special. They mean coinciding vertices.
			#Treat such ratios as positive if A element is positive.
			if self.numclass.iszero(b_j):
				#print("b_j is zero:", b_j)
				if not self.numclass.positive(a_ji):
					continue
			ratio = b_j / a_ji
			if not self.numclass.nonnegative(ratio): continue

			if best_ratio is None or ratio < best_ratio:
				best_ratio = ratio
				best_row = j
		if best_row is None:
			self.resolution = RESOLUTION_UNBOUNDED
			return False
		#print("### col:",i_lead, "row:", best_row, "ratio", best_ratio)
		#best row and column defined
		self._diagonalize_by_row_col( best_row, i_lead )
		#Update basis
		self.basis[best_row] = i_lead
		self._validate_diagonzlized()
		return True
	
	def _diagonalize_by_row_col( self, j, i ):
		"""Diagonalize problem relatively to given row and column
		j - row
		j - col
		"""
		a_ji = self.a[j][i]
		assert not self.numclass.iszero(a_ji)

		
		#normalie j'th row
		self.b[j] /= a_ji
		aj = self.a[j]
		for i1 in range(self.n):
			if i1 != i:
				aj[i1] /= a_ji
			else:
				aj[i1] = self.numclass.one()
		
		
		#clear other rows
		_subtract_scaled_row( self.c, aj, self.c[i], self.numclass)
		self.c[i] = self.numclass.zero()

		for j1, a_j1 in enumerate(self.a):
			if j1 == j: continue
			k = a_j1[i]
			_subtract_scaled_row( a_j1, aj, k, self.numclass)
			assert self.numclass.iszero(a_j1[i]) 
			a_j1[i] = self.numclass.zero() #enforce zero
			self.b[j1] -= self.b[j] * k
			

	def _show_row(self, first, row):
		print( str(first) + '\t|' + "\t".join(map(str, row)))
		
	def show(self):
		print( "Resolution:", self.resolution)
		print( "Basis:", ", ".join('x{}'.format(i) for i in self.basis))
		self._show_row('', ('x{}'.format(i) for i in range(self.n)))
		self._show_row(' C', self.c)
		for j in range(self.m):
			self._show_row(self.b[j], self.a[j])
			
def simplex_canonical( a, b, c, basis, num, verbose=False, do_coerce = True):
	"""Simplex method in canonical form, when initial basis is fully known"""
	if do_coerce:
		a = num.coerce_mtx(a)
		b = num.coerce_vec(b)
		c = num.coerce_vec(c)
		
	solver = SimplexSolver(a, b, c, basis, numclass=num, clean_c_row = True)
	if verbose:
		print("############ Regular simplex:#############")
		solver.show()
	while solver.resolution == RESOLUTION_NO:
		solver.step()
		if verbose:
			print("############ Regular step:#############")
			solver.show()

	return solver.resolution, solver.vertex()

def simplex_canonical_m( a, b, c, basis, num, verbose=False, do_coerce = True):
	"""Simplex method in canonical form, when initial basis is not fully known.
	Some or all elements of 'basis' can be None
	"""
	#apply artificial basis method.
	if do_coerce:
		a = num.coerce_mtx(a)
		b = num.coerce_vec(b)
		c = num.coerce_vec(c)
	n_artificial = sum( int(bi is None) for bi in basis )
	n = len(c)
	if n_artificial == 0:
		return simplex_canonical(a,b,c,basis, num, verbose=verbose, do_coerce=False)
	
	#Expand the problem with artificial variables
	zeros = [num.zero()]*n_artificial
	a = [ a_j+zeros for a_j in a ]
	
	i_next = n #next artificial variable
	m_basis = basis[:]
	for j, bi in enumerate(basis):
		if bi is None:
			a[j][i_next] = num.one()
			m_basis[j] = i_next
			i_next += 1
	assert i_next == n + n_artificial
	
	#modified C vector for M-problem.
	cm = [num.zero()]*n+[num.one()]*n_artificial

	#Now solve M-problem step by step, until all artificial variables not in the basis
	m_solver = SimplexSolver(a, b, cm, m_basis, num, clean_c_row=True)
	real_vertex_reached = False
	if verbose:
		print("########### M-problem: #############")
		m_solver.show()
	while m_solver.resolution == RESOLUTION_NO:
		m_solver.step()
		if verbose:
			print("########### M-step: #############")
			m_solver.show()
			
		if all( bi < n for bi in m_solver.basis):
			#m-problem solved!
			if verbose:
				print("### Real vertex reached")
			real_vertex_reached = True
			break
		
	if not real_vertex_reached:
		#M-problem solved, but no real vertex.
		if verbose:
			print("### Empty simplex")
		return RESOLUTION_INCOMPATIBLE, None
	
	#truncate m-problem back
	a = [a_row[:n] for a_row in m_solver.a]
	
	return simplex_canonical(a, m_solver.b, c, m_solver.basis, num = num, verbose=verbose, do_coerce=False)

def linsolve( objective,
			  ineq_left=(), ineq_right=(),
			  eq_left=(), eq_right=(),
			  nonneg_variables = (),
			  num=RealFiniteTolerance(),
			  verbose=False, do_coerce=True):
	""" Solve arbitrary linear programming problem:
	Minimize linear function Cx -> min under set of conditions:
		 Ax <= B
		 A'x == B'
	Arguments:
	  ineq_left : MxN matrix A
	  ineq_right: M-vector B
	  eq_left: M'xN matrix A'
	  eq_right: M'-vector B'
	  nonneg_variables: list of variables (their 0-based indices) that are >= 0
	  num: instance of NumberTypeclass, defining numeric implementation to be used. Default is RealFiniteTolerance().
	  do_coerce: if True, all provided values are converted by the 'coerce' method of the typeclass before solving the problem. Added for testing convenience.
	  verbose: if True, then solver prints solution steps to the console. Added for debug.
	Returns tuple: (resulution, x).
	Where resolution is one of RESOLUTION_SOLVED, RESOLUTION_INCOMPATIBLE, RESOLUTION_UNBOUNDED
	If resolution is RESOLUTION_SOLVED, then x contains solution vector (list). Othervise, value of x is not defined.
	"""
	nonneg_variables = set(nonneg_variables)
	n = len(objective)
	assert all( len(aj) == n for aj in ineq_left )
	assert all( len(aj) == n for aj in eq_left )

	#we will add several artificial variables: negative parts of variables, inequality-to-equality artificial variables
	#This stores index of the next fgree variable to add
	next_variable = n

	#first, create variables to map nonpositive variables
	#  x -> x1 - x2
	#  after substitution, x1 gets the same index as X, x2 gets new index

	#map variable index -> variable index, maps x1 to x2.
	negative_part2positive_part = {} 

	for var in range(n):
		if var not in nonneg_variables:
			var_neg = next_variable
			next_variable += 1
			negative_part2positive_part[var_neg] = var

	n_nonneg = next_variable
	if verbose:
		print ("Created", n_nonneg-n, "new varables to get rid of variables with unknown sign")

	def positivise_row( row ):
		#map row of A pr C matrix to positive variables
		return [ row[i] if i not in negative_part2positive_part else -row[negative_part2positive_part[i]]
				 for i in range(n_nonneg) ]

	#Get rid of nonpositive variablesby substitution
	c_nonneg = positivise_row(objective)
	ineq_left_nonneg = list(map(positivise_row, ineq_left))
	eq_left_nonneg = list(map(positivise_row, eq_left))
	
	def negate_row( row ): return [-ri for ri in row]

	#Number of artificial Z variables is the same as number of inequalities
	num_inequalities = len(ineq_left)
	assert len(ineq_right) == num_inequalities

	a_extended = []
	b_extended = []
	basis = []
	
	#Get rid of inequalities
	for a_row, bi in zip(ineq_left_nonneg, ineq_right):
		# Inequality (Ai, x) <= bi
		a_row_extended = a_row + [num.zero()]*num_inequalities
		artificial_var = next_variable
		next_variable += 1
		a_row_extended[artificial_var] = num.one()
		#changed Ax <= b
		# to Ax + z <= b

		#Now ensure that b >= 0
		if not num.nonnegative( bi ):
			bi = -bi
			a_row_extended = negate_row(a_row_extended)
			#Since this row was negated, this artificial variable can't be added to the basis.
			basis.append(None) 
		else:
			#if the variable is not negated, then it can be added to the basis.
			basis.append(artificial_var)
		a_extended.append( a_row_extended )
		b_extended.append( bi )
		
	#And copy equalities, as-is, extending with zeros.
	for a_row, bi in zip(eq_left_nonneg, eq_right):
		if not num.nonnegative( bi ):
			bi = -bi
			a_row = negate_row(a_row)
		a_extended.append( a_row + [num.zero()]*num_inequalities )
		b_extended.append( bi )
		basis.append(None)

	#Phew! DOne!
	#Now a_extended, b_extended, c_nonneg describe the problem.
	#basis stores indices of varaibles that are already diagonalized.
	#Solve it!

	resolution, solution = simplex_canonical_m(a_extended,
											   b_extended,
											   c_nonneg+[num.zero()]*num_inequalities,
											   basis,
											   num=num,
											   verbose=verbose,
											   do_coerce = do_coerce)

	if resolution == RESOLUTION_SOLVED:
		#Remove artificial variables
		orig_solution = solution[:n]
		
		#then fold back positive-negative pairs
		for negative_var, positive_var in negative_part2positive_part.items():
			orig_solution[positive_var] -= solution[negative_var]

		return resolution, orig_solution
	else:
		return resolution, solution

	
# python3
from sys import stdin

n, m = list(map(int, stdin.readline().split()))
A = []
for i in range(m):
	A.append([0]*i+[-1]+[0]*(m-i-1))
for i in range(n):
	A += [list(map(int, stdin.readline().split()))]
b = list(map(int, stdin.readline().split()))
c = list(map(int, stdin.readline().split()))
c = list(map(lambda x:-x, c))
b = [0]*m + b
resolution, sol = linsolve( c, ineq_left = A, ineq_right = b)

if resolution == "incompatible":
	print("No solution")
if resolution == "solved":  
	print("Bounded solution")
	print(' '.join(list(map(lambda x : '%.6f' % x, sol))))
if resolution == "unbounded":
	print("Infinity")