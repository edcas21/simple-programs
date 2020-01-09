
def asciiConvert(char):
	
	if char.isdigit() == True:
		return int(char)
	else:
		return ord(char)
		

def mult_persistence(strNum):
	
	strNumLen = len(strNum)
	perProd = 0
	iter = 0
	
	while strNumLen > 1:
		i = 0
		for dig in strNum:
			
			num = asciiConvert(dig)
			
			if i == 0:
				perProd  =  num
			else:
				perProd *= num
				
			if i < (strNumLen - 1):
				print(num, "*", end = " ")
			else:
				print(num, "=", perProd)
			
			i += 1
		
		print()
		
		iter += 1
		strNum = str(perProd)
		perProd = 0
		strNumLen = len(strNum)
	
	return iter

def add_persistence(strNum):
	
	strNumLen = len(strNum)
	perSum = 0
	iter = 0
	
	while strNumLen > 1:
		
		i = 0
		
		for dig in strNum:
			
			num = asciiConvert(dig)
			perSum += num
			
			if(i < strNumLen - 1):
				print(num, "+", end = " ")
			else:
				print(num, "=", perSum)
				
			i += 1
			
		print()
		
		iter += 1
		strNum = str(perSum)
		perSum = 0
		strNumLen = len(strNum)
	
	return iter
			
#Main
stop = False

while stop == False:
	
	print("Enter -1 to exit.")
	num = input("Enter a number: ")
	print()
	
	if(num != -1):
		print("additive peristence:", add_persistence(num))
		print()
		print("multiplicative peristence:", mult_persistence(num))
		print()		
	else:
		stop = True