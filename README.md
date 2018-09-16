# Illumio Coding Assignment, PCE team (Avenger)

Submission by : Eric Zhou 

## Solution design
I created a class in java (Firewall.java). 
I used the Buffered Reader library to parse the CSV file containing the rules. I went through line by line to figure out what 
to add to my HashMap. I used  HashMap<String, ArrayList<String>> because I wanted to be able to have a single value that maps
to multiple keys. The key is the concatended string of the first two parameters: direction and protocol and the value is the 
ArrayList of rules that have those two in them. This map gets returned and then when checking for acceptance, the acceptance 
method checks to see if the ip or port has a range. If the port or ip parameter is in the range of the ip, then return true. 

I decided to use a HashMap to ensure quick lookups and inserts. Since I stored strings in the hashmap, I would use parseInt or 
parseLong to get the ranges.

Through my research, I also found that ip addresses can be converted to longs so when checking to see if ip addresses are in 
the range, it is a simple comparison of the parameter ipaddress and range ip address.

## Potential changes with more time

If I had more time, I would have liked to increase the efficiency of the for loop that goes through every single subrule for 
a certain key. This could take a significant amount of time if the arraylist is very large.

I would create separate methods for checking to see if port & ip are in the range.


### Testing 
For my testing, I just did a few checks in a main method utilizing a csv. However, this CSV could be nowhere near the amount 
of rules a real csv could have so it was mainly used to check for basic functionality. I checked on some common edge cases: 
null, empty strings, MAX_INTEGER, etc. to see if those would work. 


### Teams Preference
1. Policy
2. Platform
