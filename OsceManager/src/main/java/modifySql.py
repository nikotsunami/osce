#!/usr/bin/python

import re

file = open('patients.sql')
out = ""
for line in file:
	#matched = re.search("INSERT INTO `[a-zA-Z_]+`(\\.`[a-zA-Z_]+`)?\s*\\(", line)
	tokens = re.split("\\(", line)
	outLine = ""
	if (len(tokens) > 2):
		outLine += tokens[0] + "(" + tokens[1] + "("
		values = re.split(",", tokens[2])
		rng = range(len(values))
		for i in rng:
			value = values[i];
			if (i == 20):
				value = values[0]
			outLine += value.strip()
			if (value != values[len(values)-1]):
				outLine += ","
		out += outLine + "\n"
print out
