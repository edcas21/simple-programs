import math

def collatz(num):
	
	res = num
	sequence = [ ]
	cond = True
	sequence.append(num)
	
	while cond:
	
		if(res % 2) == 0:
			res /= 2
			sequence.append(int(res))
		else:
			res = (res * 3) + 1
			sequence.append(int(res))
		
		if res == 1:
			cond = False
	
	#Output
	print("collatz(%d) --> (%d, %d)" % (num, max(sequence), len(sequence)))
	print("# seq =", sequence)
	print()
	
#Main

stop = False

while stop == False:
	
	print("Enter -1 to exit.")
	num = int(input("Enter a number: "))
	print()
	
	if(num != -1):
		collatz(num)
	else:
		stop = True