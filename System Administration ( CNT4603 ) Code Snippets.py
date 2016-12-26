#------------------------------------------------------------------
# Project 7 Python Script
#
# ipaddress.py
#------------------------------------------------------------------

#------------------------------------------------------------------
# Variables
#------------------------------------------------------------------

#I don't feel like typing these over and over so I'm going to store the string

#Message for an invalid match
invalidIP = "Error - no match  - invalid IP address:  ";

#Message for a valid match
validIP = "Match found - valid IP address:  ";

#Get user input for file name
fileName = input("Please enter the name of the file containing the IP addresses: ");
#Professor never said anything about it but the fileName needs to be absolute
#For example "ipin" is not valid but "ipin.txt" is

#Skip a line
print("\n");

#------------------------------------------------------------------
# Script Body
#------------------------------------------------------------------

#First I need to open the file
with open(fileName) as file:
    #Next I am going to read through the file line by line
    for line in file:
        #First of all, if the string does not contain a "." we know it's invalid
        if ("." not in line):
            print (invalidIP+line);
        else:
            #Next, we know that if it does not contain exactly 3 "."s, it's invalid
            if (line.count('.') == 3):
                #Now we split the string by "."s and check the length of each one.  If it's greater than 3, then we know it's invalid
                if (not len(line.split('.')[0]) > 3 or not len(line.split('.')[1]) > 3 or not len(line.split('.')[2]) > 3 or not len(line.split('.')[3]) > 3):
                    print(validIP+line);
                else:
                    print(invalidIP+line);
            else:
                print(invalidIP+line);
#Skip a line
print("\n");

#------------------------------------------------------------------
# Script End
#------------------------------------------------------------------

####################################################################

#------------------------------------------------------------------
# Project 7 Python Script
#
# naPhones.py
#------------------------------------------------------------------

#------------------------------------------------------------------
# Variables
#------------------------------------------------------------------

#I don't feel like typing these over and over so I'm going to store the string

#Message for an invalid match
invalidPhone = "Error - no match  - invalid North American phone number:  ";

#Message for a valid match
validPhone = "Match found - valid North American phone number:  ";

#Get user input for file name
fileName = input("Please enter the name of the file containing the input North American phone numbers: ");

#Skip a line
print("\n");

#------------------------------------------------------------------
# Script Body
#------------------------------------------------------------------

#First I need to open the file
with open(fileName) as file:
    #Next I am going to read through the file line by line
    for line in file:
        #Creating a new string because I want to use both for testing purposes
        newLine = line;
        
        #First of all, let's strip everything that is not a number
        for c in ['-', '.', '(', ')', ' ', '\n']:
            if c in line:
                newLine = newLine.replace(c, "");
        #Now firstly, there must be 10 digits for this to be valid
        if (len(newLine) < 10 or len(newLine) > 10):
            print(invalidPhone+line);
        else:
            #Secondly, if the first character of area code [0]
            #or prefix [3] is 1 or 0 then it's invalid
            if (newLine[0] == '0' or newLine[0] == '1' or newLine[3] == '0' or newLine[3] == '1'):
                print(invalidPhone+line);
            else:
                #Finally, if the first character is a '('
                #but the 4th character isn't a ')' or vice verse,
                #then it's invalid
                if ((line[0] == '(' and not line[4] == ')') or (not line[0] == '(' and line[4] == ')')):
                    print(invalidPhone+line);
                else:
                    print(validPhone+line);
#Skip a line
print("\n");

#------------------------------------------------------------------
# Script End
#------------------------------------------------------------------

###################################################################

#-------------------------------------------
# Project 7 Powershell Script
#
# getSearchedServices();
#-------------------------------------------

#-------------------------------------------
# Variables
#-------------------------------------------

#Get input from command line

param([string]$string);

#Skip a line
write-host "";

#-------------------------------------------
# Script Body
#-------------------------------------------

#Outputting some initial text to the screen

write-host "The following $string services are currently running on: " -foregroundcolor "red" -nonewline;
write-host $env:computername -foregroundcolor "magenta";

#Outputting the services by name and display name

get-service $string -ErrorAction SilentlyContinue -ErrorVariable getServiceError| where-object {$_.status -eq "running"} | format-table -property name, displayname;

#What if a process is inserted that's not there? (Added error parameters to get-service)

if ($getServiceError)
{
	#Skip a line
	write-host "";

	write-host "No such process";
	
	#Skip a line
	write-host "";
}
write-host "List of running services complete" -foregroundcolor "blue";
write-host "Script terminating..." -foregroundcolor "red";

#Skip a line
write-host "";
#-------------------------------------------
# END SCRIPT
#-------------------------------------------
