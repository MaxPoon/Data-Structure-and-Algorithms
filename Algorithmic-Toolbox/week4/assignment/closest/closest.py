#Uses python3
import sys
import math
def distance(point_1, point_2):
	return ((point_1[0] - point_2[0])**2 + (point_1[1] - point_2[1])**2)**0.5

def minimum_distance(points, start, end):
	#write your code here
	if end - start ==1:
		if points[start][1] > points[end][1]:
			points[start], points[end] = points[end], points[start]
		return distance(points[start], points[end])
	if end - start == 2:
		tempPoints = sorted(points[start:end+1], key= lambda x: x[1])
		for i in range(3):
			points[start+i] = tempPoints[i]
		return min(distance(points[start], points[start+1]), distance(points[start], points[end]), distance(points[end], points[start+1]))
	mid = int((start + end)/2)
	dl = minimum_distance(points, start, mid+1)
	dr = minimum_distance(points, mid+1, end)
	dmin = min(dl, dr)
	line = points[mid+1][0]
	left = start
	right = mid + 1
	# tempPoints = []
	# while left<= mid or right<= end:
	# 	if left>mid or points[left][1]>points[right][1]:
	# 		tempPoints.append(points[right])
	# 		right += 1
	# 	else:
	# 		tempPoints.append(points[left])
	# 		left += 1
	tempPoints = sorted(points[start:end+1], key= lambda x: x[1])
	for i in range(len(tempPoints)):
		points[start+i] = tempPoints[i]
	line = points[mid+1][0]
	strip = []
	for point in points[start:end+1]:
		if abs(line-point[0])>dmin: continue
		strip.append(point)
	for i in range(len(strip)):
		point = strip[i]
		for j in range(1,4):
			if i+j>=len(strip): break
			dmin = min(dmin, distance(point,strip[i+j]))
	return dmin


if __name__ == '__main__':
	input = sys.stdin.read()
	data = list(map(int, input.split()))
	n = data[0]
	x = data[1::2]
	y = data[2::2]
	points = [[x[i], y[i]] for i in range(len(x))]
	points.sort()
	print("{0:.9f}".format(minimum_distance(points, 0, len(points)-1)))
