# python3
import heapq
class Worker:
    def __init__(self, thread_id, release_time=0):
        self.thread_id = thread_id
        self.release_time = release_time

    def __lt__(self, other):
        if self.release_time == other.release_time:
            return self.thread_id < other.thread_id
        return self.release_time < other.release_time

    def __gt__(self, other):
        if self.release_time == other.release_time:
            return self.thread_id > other.thread_id
        return self.release_time > other.release_time

class JobQueue:
	def read_data(self):
		self.num_workers, m = map(int, input().split())
		self.jobs = list(map(int, input().split()))
		assert m == len(self.jobs)

	def write_response(self):
		for i in range(len(self.jobs)):
			print(self.assigned_workers[i], self.start_times[i]) 

	def assign_jobs(self):
		# TODO: replace this code with a faster algorithm.		
		self.assigned_workers = [None] * len(self.jobs)
		self.start_times = [None] * len(self.jobs)
		worker_queue = [Worker(i) for i in range(self.num_workers)]
		for i in range(len(self.jobs)):
			worker = heapq.heappop(worker_queue)
			self.assigned_workers[i] = worker.thread_id
			self.start_times[i] = worker.release_time
			worker.release_time += self.jobs[i]
			heapq.heappush(worker_queue, worker)

	def solve(self):
		self.read_data()
		self.assign_jobs()
		self.write_response()

if __name__ == '__main__':
	job_queue = JobQueue()
	job_queue.solve()

