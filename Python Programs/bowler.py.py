
class Frame:
	def __init__(self):
		self.roll1 = 0
		self.roll2 = 0
		self.frame_score = 0
	
	def firstRoll(self, pins):
		if pins in  "xX":
			self.roll1 = 10
		else:
			self.roll1 = int(pins)
	
	def secRoll(self, pins):
		if pins == '/':
			self.roll2 = 10 - self.roll1
		else:
			self.roll2 = pins
	
	def setScore(self, score):
		self.frame_score = score
	
	def getScore(self):
		return self.frame_score
		
class Game:
	def __init__(self):
		self.frames = []
	
	def add(self, frame):
		self.frames.append(frame)
		
	def editFrame(self, frameNum, update):
		self.frames[frameNum] = update
	
	def editLast(self, update):
		length = len(self.frames)
		self.frames[length - 1] = update
	
	def deleteFrame(self, frameNum):
		del self.frames[frameNum]
	
	def deleteLast(self):
		length = len(self.frames)
		del self.frames[length - 1]
		
	def getFinalScore(self):
		return self.frames[len(self.frames) - 1].getScore

def convert(pin_list, index):
	
	val = 0
	
	if pin_list[index] in "xX":
			val = 10
	elif pin_list[index] == "/":
		if index > 0:
			val = 10 - int(pin_list[index - 1])
	else:
		val = int(pin_list[index])
	
	return val
	

def calcScore(pinList):
	
	game = Game()
	curScore = 0
	length = len(pinList)
	lastIndex = length - 1
	frameNum = 0
	i = 0
	
	while i <  len(pinList):
		
		pins = convert(pinList, i)
		print(pins)
		
		if i <= (lastIndex - 3):
			
			right1 = convert(pinList, i + 1)
			right2 = convert(pinList, i + 2)
				
			#strike
			if pins == 10:
				print("strike")
				newFrame = Frame()
				newFrame.firstRoll = pins
				curScore = pins + right1 + right2
				newFrame.setScore(curScore)
				game.add(newFrame)
				frameNum += 1
			
			#spare
			if pins < 10:
				print("spare")
				newFrame = Frame()
				newFrame.firstRoll = pins
				newFrame.secRoll = right1
				
				if (pins + right1) == 10:
					curScore += 10 + right1
					newFrame.setScore(curScore)
					
			#open frame
				else:
					print("open frame")
					curScore += pins + right1
					newFrame.setScore(curScore)
				
				game.add(newFrame)
				frameNum += 1
				i += 2
				continue	
		
		#10th frame	
		else:
			newFrame = Frame()
			curScore += pins
			game.add(newFrame)
			
		i += 1
			
	return game

#def save(game):
	
game = calcScore(["10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10"])
#print(int("10"))

"""array = ["10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10"]

for i in array:
	print(int(i))"""

print(game.getFinalScore())

#def restore():
	

	