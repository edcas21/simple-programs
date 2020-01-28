import math
import os
import random
import re
import sys

""" Function to determine the hour glass figure that has the highest sum within in a 2D list"""
def hourglassSum(arr):

    maxHourglassSum = 0
    hgSum = 0
    hgNum = 0
    
    for r in range(len(arr) - 1):
        topCone = r - 1
        botCone = r + 1
        for c in range(1, len(arr[r]) - 1):
            # Hourglass neck value
            neck = arr[r][c]
            # Hourglass top cone sum
            sumTopCone = arr[topCone][c] + arr[topCone][c + 1] + arr[topCone][c - 1]
            # Hourglass bottom cone sum
            sumBotCone = arr[botCone][c] + arr[botCone][c + 1] + arr[botCone][c - 1]
            
            # Add all the values together
            hgSum = neck + sumTopCone + sumBotCone
            
            # Count the hourglass figures
            hgNum += 1
            
            # Start the hourglass sum at a representative value
            if hgNum == 1:
                maxHourglassSum = hgSum
            
            # Update max
            if hgSum > maxHourglassSum:
                maxHourglassSum = hgSum
    
    return maxHourglassSum
    
    if __name__ == '__main__':
        fptr = open(os.environ['OUTPUT_PATH'], 'w')
        arr = []
        
        for _ in range(6):
            arr.append(list(map(int, input().rstrip().split())))
        
        result = hourglassSum(arr)
        
        fptr.write(str(result) + '\n')
        
        fptr.close()
