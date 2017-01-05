# python3
from decimal import Decimal, getcontext
from copy import deepcopy
getcontext().prec = 30

import math

class Vector(object):
	def __init__(self, coordinates):
		try:
			if not coordinates:
				raise ValueError
			self.coordinates = tuple(coordinates)
			self.dimension = len(coordinates)

		except ValueError:
			raise ValueError('The coordinates must be nonempty')

		except TypeError:
			raise TypeError('The coordinates must be an iterable')


	def __str__(self):
		return 'Vector: {}'.format(self.coordinates)


	def __eq__(self, v):
		return self.coordinates == v.coordinates

	def __add__(self, other):
		if type(other) == Vector:
			try:
				assert self.dimension == other.dimension
			except Exception:
				raise AssertionError("The length of the vectors must be the same")
			newList = [self.coordinates[i]+other.coordinates[i] for i in range(self.dimension)]
			return Vector(newList)
		if type(other) == int or type(other) == float:
			newList = [self.coordinates[i]+other for i in range(self.dimension)]
			return Vector(newList)

	def __radd__(self, other):
		if type(other) == Vector:
			try:
				assert self.dimension == other.dimension
			except Exception:
				raise AssertionError("The length of the vectors must be the same")
			newList = [self.coordinates[i]+other.coordinates[i] for i in range(self.dimension)]
			return Vector(newList)
		if type(other) == int or type(other) == float:
			newList = [self.coordinates[i]+other for i in range(self.dimension)]
			return Vector(newList)

	def __sub__(self, other):
		if type(other) == Vector:
			try:
				assert self.dimension == other.dimension
			except Exception:
				raise AssertionError("The length of the vectors must be the same")
			newList = [self.coordinates[i]-other.coordinates[i] for i in range(self.dimension)]
			return Vector(newList)
		if type(other) == int or type(other) == float:
			newList = [self.coordinates[i]-other for i in range(self.dimension)]
			return Vector(newList)

	def __rsub__(self, other):
		if type(other) == int or type(other) == float:
			newList = [other - self.coordinates[i] for i in range(self.dimension)]
			return Vector(newList)

	def __mul__(self, other):
		if type(other) == Vector:
			try:
				assert self.dimension == other.dimension
			except Exception:
				raise AssertionError("The length of the vectors must be the same")
			newList = [self.coordinates[i]*other.coordinates[i] for i in range(self.dimension)]
			return sum(newList)
		if type(other) == int or type(other) == float:
			newList = [self.coordinates[i]*other for i in range(self.dimension)]
			return Vector(newList)

	def __rmul__(self, other):
		if type(other) == Vector:
			try:
				assert self.dimension == other.dimension
			except Exception:
				raise AssertionError("The length of the vectors must be the same")
			newList = [self.coordinates[i]*other.coordinates[i] for i in range(self.dimension)]
			return sum(newList)
		if type(other) == int or type(other) == float:
			newList = [self.coordinates[i]*other for i in range(self.dimension)]
			return Vector(newList)

	def __truediv__(self, other):
		if type(other) == Vector:
			try:
				assert self.dimension == other.dimension
			except Exception:
				raise AssertionError("The length of the vectors must be the same")
			newList = [self.coordinates[i]/other.coordinates[i] for i in range(self.dimension)]
			return Vector(newList)
		if type(other) == int or type(other) == float:
			newList = [self.coordinates[i]/other for i in range(self.dimension)]
			return Vector(newList)

	def __pow__(self, other):
		if type(other) == Vector:
			try:
				assert self.dimension == 3 and other.dimension==3
			except Exception:
				raise AssertionError("The length of the vectors must be the 3")
			x1,y1,z1 = self.coordinates
			x2,y2,z2 = other.coordinates
			return Vector([y1*z2-y2*z1, z1*x2-z2*x1, x1*y2-x2*y1])

		else:
			raise TypeError("The parameter must be vector")

	def __len__(self):
		return len(self.coordinates)

	def __getitem__(self, i):
		return self.coordinates[i]

	def __setitem__(self, i, x):
		self.coordinates[i] = x

	def __deepcopy__(self, memo):
		copiedCoordinates = deepcopy(self.coordinates)
		return Vector(copiedCoordinates)

	def mag(self):
		return sum(n**2 for n in self.coordinates)**0.5

	def unit(self):
		m = self.mag()
		if m == 0:
			return self
		return self / m

	def sim(self, other):
		if type(other) != Vector:
			raise Exception("The parameter must be vector")			
		return (self*other)/(self.mag()*other.mag())

	def angle(self, other, degree = False):
		if type(other) != Vector:
			raise Exception("The parameter must be vector")
		a = math.acos(self.sim(other))
		if degree:
			return a * (360/math.pi)
		return a

	def parallel(self, other):
		if type(other) != Vector:
			raise Exception("The parameter must be vector")
		u1 = self.unit()
		u2 = other.unit()
		if u1==u2 or (u1+u2).mag()==0: return True
		return False

	def orthogonal(self, other, error=0.0001):
		if type(other) != Vector:
			raise Exception("The parameter must be vector")
		return self*other <= error

	def projectTo(self, other):
		if type(other) != Vector:
			raise Exception("The parameter must be vector")
		u = other.unit()
		return u * (self * u)

class LinearSystem(object):

	NO_UNIQUE_SOLUTIONS_MSG = 'No unique solutions'

	def __init__(self, equations):
		try:
			d = equations[0].dimension
			for eq in equations:
				assert eq.dimension == d

			self.equations = equations
			self.dimension = d

		except AssertionError:
			raise Exception(self.ALL_EQUATIONS_MUST_BE_IN_SAME_DIM_MSG)


	def swap_rows(self, row1, row2):
		self[row1], self[row2] = self[row2], self[row1]

	def multiply_coefficient_and_row(self, coefficient, row):
		self[row] *= coefficient


	def add_multiple_times_row_to_row(self, coefficient, row_to_add, row_to_be_added_to):
		self[row_to_be_added_to] += coefficient * self[row_to_add]


	def indices_of_first_nonzero_terms_in_each_row(self):
		num_equations = len(self)
		num_variables = self.dimension

		indices = [-1] * num_equations

		for i,p in enumerate(self.planes):
			try:
				indices[i] = p.first_nonzero_index(p.normal_vector)
			except Exception as e:
				if str(e) == Plane.NO_NONZERO_ELTS_FOUND_MSG:
					continue
				else:
					raise e

		return indices


	def __len__(self):
		return len(self.equations)


	def __getitem__(self, i):
		return self.equations[i]


	def __setitem__(self, i, x):
		try:
			assert x.dimension == self.dimension
			self.equations[i] = x

		except AssertionError:
			raise Exception(self.ALL_EQUATIONS_MUST_BE_IN_SAME_DIM_MSG)


	def __str__(self):
		ret = 'Linear System:\n'
		temp = ['Equation {}: {}'.format(i+1,p) for i,p in enumerate(self.equations)]
		ret += '\n'.join(temp)
		return ret

	def __deepcopy__(self, memo):
		copiedEquations = []
		for equation in self.equations:
			copiedEquation = deepcopy(equation)
			copiedEquations.append(copiedEquation)
		return LinearSystem(copiedEquations)

	def triangularForm(self):
		if len(self) < self.dimension: return False
		copied = deepcopy(self)
		for i in range(self.dimension):
			if copied[i][i]==0:
				found = False
				for j in range(i+1, copied.dimension):
					if copied[j][i] != 0:
						found = True
						break
				if not found: return False
				copied.swap_rows(i, j)
			copied[i] /= copied[i][i]
			for j in range(i+1, copied.dimension):
				copied.add_multiple_times_row_to_row(-copied[j][i], i, j)
		return copied

	def solve(self):
		triangle = self.triangularForm()
		if not triangle: return self.NO_UNIQUE_SOLUTIONS_MSG
		for i in range(triangle.dimension):
			if not triangle[i].valid(): return self.NO_UNIQUE_SOLUTIONS_MSG
		for i in range(self.dimension-1, -1, -1):
			for j in range(i):
				triangle.add_multiple_times_row_to_row(-triangle[j][i], i, j)
		result = [0] * triangle.dimension
		for i in range(triangle.dimension):
			result[i] = triangle[i].constant_term
		return result


class Lineq(object):

	NO_NONZERO_ELTS_FOUND_MSG = 'No nonzero elements found'

	def __init__(self, normal_vector, constant_term=0):
		try:
			assert type(normal_vector) == list or type(normal_vector) == tuple \
				or type(normal_vector) == Vector
		except AssertionError:
			raise AssertionError("The normal vector must be a list, tuple or vector object")
		try:
			assert type(constant_term) == int or type(constant_term) == float
		except AssertionError:
			raise AssertionError("The constant term must be a number")
		if type(normal_vector) == list or type(normal_vector) == tuple:
			normal_vector = Vector(normal_vector)
		self.dimension = normal_vector.dimension
		self.normal_vector = normal_vector
		self.constant_term = constant_term

		self.set_basepoint()

	def __add__(self, other):
		if type(other) == Lineq:
			try:
				assert self.dimension == other.dimension
			except Exception:
				raise AssertionError("The dimensions of the equations must be the same")
			new_normal_vector = self.normal_vector + other.normal_vector
			new_constant = self.constant_term + other.constant_term
			return Lineq(new_normal_vector, new_constant)

	def __radd__(self, other):
		if type(other) == Lineq:
			try:
				assert self.dimension == other.dimension
			except Exception:
				raise AssertionError("The dimensions of the equations must be the same")
			new_normal_vector = self.normal_vector + other.normal_vector
			new_constant = self.constant_term + other.constant_term
			return Lineq(new_normal_vector, new_constant)

	def __sub__(self, other):
		if type(other) == Lineq:
			try:
				assert self.dimension == other.dimension
			except Exception:
				raise AssertionError("The dimensions of the equations must be the same")
			new_normal_vector = self.normal_vector - other.normal_vector
			new_constant = self.constant_term - other.constant_term
			return Lineq(new_normal_vector, new_constant)

	def __mul__(self, other):
		if type(other) == int or type(other) == float:
			new_normal_vector = self.normal_vector * other
			new_constant = self.constant_term * other
			return Lineq(new_normal_vector, new_constant)

	def __rmul__(self, other):
		if type(other) == int or type(other) == float:
			new_normal_vector = self.normal_vector * other
			new_constant = self.constant_term * other
			return Lineq(new_normal_vector, new_constant)

	def __truediv__(self, other):
		if type(other) == int or type(other) == float:
			new_normal_vector = self.normal_vector / other
			new_constant = self.constant_term / other
			return Lineq(new_normal_vector, new_constant)

	def __len__(self):
		return len(self.normal_vector)

	def __getitem__(self, i):
		return self.normal_vector[i]

	def __setitem__(self, i, x):
		self.normal_vector[i] = x

	def __deepcopy__(self, memo):
		copiedVector = deepcopy(self.normal_vector)
		return Lineq(copiedVector, self.constant_term)

	def valid(self):
		if self.constant_term == 0: return True
		for i in range(len(self)):
			if self[i] != 0: return True
		return False

	def set_basepoint(self):
		try:
			n = self.normal_vector
			c = self.constant_term
			basepoint_coords = [0]*self.dimension

			initial_index = Lineq.first_nonzero_index(n.coordinates)
			initial_coefficient = n.coordinates[initial_index]

			basepoint_coords[initial_index] = c/initial_coefficient
			self.basepoint = Vector(basepoint_coords)

		except Exception as e:
			if str(e) == Lineq.NO_NONZERO_ELTS_FOUND_MSG:
				self.basepoint = None
			else:
				raise e

	def __eq__(self, other):
		if not type(other) == Lineq:
			raise TypeError("The parameter should be a Lineq object")
		if self.dimension != other.dimension:
			return False
		if not self.parallel(other): return False
		s = 0
		for i in range(self.dimension):
			s += other.normal_vector.coordinates[i]*self.basepoint.coordinates[i]
		return s==other.constant_term

	def __str__(self):

		num_decimal_places = 3

		def write_coefficient(coefficient, is_initial_term=False):
			coefficient = round(coefficient, num_decimal_places)
			if coefficient % 1 == 0:
				coefficient = int(coefficient)

			output = ''

			if coefficient < 0:
				output += '-'
			if coefficient > 0 and not is_initial_term:
				output += '+'

			if not is_initial_term:
				output += ' '

			if abs(coefficient) != 1:
				output += '{}'.format(abs(coefficient))

			return output

		n = self.normal_vector.coordinates

		try:
			initial_index = Lineq.first_nonzero_index(n)
			terms = [write_coefficient(n[i], is_initial_term=(i==initial_index)) + 'x_{}'.format(i+1)
					 for i in range(self.dimension) if round(n[i], num_decimal_places) != 0]
			output = ' '.join(terms)

		except Exception as e:
			if str(e) == self.NO_NONZERO_ELTS_FOUND_MSG:
				output = '0'
			else:
				raise e

		constant = round(self.constant_term, num_decimal_places)
		if constant % 1 == 0:
			constant = int(constant)
		output += ' = {}'.format(constant)

		return output
	
	def parallel(self, other):
		if not type(other) == Lineq:
			raise TypeError("The parameter should be a Lineq object")
		if self.dimension != other.dimension:
			raise TypeError("The dimensions of the equations should be the same")
		return self.normal_vector.parallel(other.normal_vector)


	@staticmethod
	def first_nonzero_index(iterable):
		for k, item in enumerate(iterable):
			if not MyDecimal(item).is_near_zero():
				return k
		raise Exception(Lineq.NO_NONZERO_ELTS_FOUND_MSG)

class MyDecimal(Decimal):
	def is_near_zero(self, eps=1e-10):
		return abs(self) < eps

EPS = 1e-6
PRECISION = 20



def ReadEquation():
	size = int(input())
	equations = []
	for row in range(size):
		line = list(map(float, input().split()))
		equations.append(Lineq(line[:size],line[size]))
	return LinearSystem(equations)

def PrintColumn(column):
    size = len(column)
    for row in range(size):
        print("%.20lf" % column[row])

if __name__ == "__main__":
	s = ReadEquation()
	result = s.solve()
	PrintColumn(result)
