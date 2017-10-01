######################################
##      CLOUDLINUX PHP EDITOR	    ##
######################################
#! /bin/bash

## Color variables ##
color_red="\e[31m";
color_green="\e[32m";
color_yellow="\e[33m";
color_white="\e[37m";
color_cyan="\e[36m";
color_purple="\e[35m";

## Functions ##

### getUser() => Allows for the ability to switch between users ###
getUser()
{
 while [[ ! $(awk '{print $2}' /etc/userdomains | grep -E "^${user}$") || "$user" == "nobody" ]]; do
        echo -en "\n${color_white}For which account would you like to edit the php settings? ";
        read user;
        if [[ ! $(awk '{print $2}' /etc/userdomains | grep -E "^${user}$") || "$user" == "nobody" ]]; then
                echo -e "\n${color_red}You have entered an invalid user!${color_white}";
        fi
 done;
}

### header() => Just pre-formatted text that I reuse ###
## Variables ##
#    - pad = number used to determine character padding
#    - fill = pretty obvious that it's a string containing "="
#    - math = an arithmetic to help with my padding
header()
{
 pad=$(echo "$1" | wc -m);
 fill="===================================================";
 math=$(($pad+$(($pad/2))+1));
 echo -e "${color_green}${fill}" | cut -c 1-$math;
 printf " %s\n" "$1";
 echo -e "${color_green}${fill}${color_white}" | cut -c 1-$math;
}

### preCheck() => Preliminary checks prior to actually initiaiting the script ###
#     - Is this a CloudLinux server?
#     - Is the user within CageFS?
preCheck()
{
 if [[ $(awk '{print $1}' /etc/redhat-release) != "CloudLinux" ]]; then
        echo -e "\n${color_red}This is not a CloudLinux Server!${color_white}";
        exit;
 fi

 echo -e "${color_yellow}CloudLinux Easy PHP Editor${color_white}";
 getUser;

 if [[ $(cagefsctl --list-disabled | grep $user) ]]; then
        echo -e "\n${colow_red}This user is not enabled in CageFS!${color_white}";
        echo -en "\nDo you want to enable this user in CageFS [ y/n ]? ";
        read input;
        if [[ "$input" == "y" || "$input" == "Y" ]]; then
                /usr/sbin/cagefsctl --enable $user;
        else
                echo -e "\n${color_red}Exiting script...${color_white}";
                exit;
        fi
 fi
}

### phpVersion() => Allows the ability to change php versions based off of what is offered by the CloudLinux version ###
## Variables ##
#    - versions = list of php versions offered by the OS
phpVersion()
{
 echo -e "\n${color_yellow}$user is currently set to: ${color_purple}$(selectorctl --user-current --user=$user | awk '{print $1}')";
 header "Available Versions";
 versions=$(selectorctl --list | awk '{print $1}');
 for i in $versions; do
        echo -e "${color_cyan}Version: ${color_white}$i";
 done;
 echo -e "${color_cyan}Version: ${color_white}native";
 echo -ne "Please select a version: ";
 read input;
 if [[ $(echo "$versions" | grep -E "^${input}$") || "$input" == "native" ]]; then
  selectorctl --set-user-current=$input --user=$user;
  echo -e "${color_yellow}\n$user PHP successfully changed to: ${color_purple}$input!${color_white}";
 elif [[ "$input" == "" ]]; then
  return 0;
 else
  echo -e "\n${color_red}$input is an invalid input!${color_white}";
 fi
}

### phpOptions() => Shows the options available as per the selected version ###
#    - This is based off the /etc/cl.selector/php.conf and kind of assumes that it isn't broken so... yeah
## Variables ##
#    - version = user's current select php version
#    - ii = I use this as part of my for loop just so they I can use modulus math and help set up my printf ( decide when to go to a newline )
#    - options = all of the php options in which you can edit ( founded by the /etc/cl.selector/php.conf file )
phpOptions()
{
 # First some variables
 version=$(selectorctl --user-current --user=$user | awk '{print $1}');
 ii=0;
 options=$(grep -E "Directive" /etc/cl.selector/php.conf | awk '{print $3}');
 
 # Check version; if native, then no
 if [[ $version == "native" ]]; then
	echo -e "\n${color_red}You cannot edit options while using the native version!${color_white}";
	return 0;
 fi

 # Next display the options
 header "PHP Options";
 echo -ne "${color_cyan}";
 for i in $options; do
	printf "%s %5s" "$i" && ((ii+=1));
	if [[ $((ii%3)) == 0 ]]; then
	 	echo "";
	fi
 done;
 echo -ne "\n${color_white}Which of these would you like to change: ";
 read input;
 if [[ $(echo "$options" | grep -E "^${input}$") ]]; then
	phpOptionSelect "$input";
 elif [[ "$input" == "" ]]; then
	return 0;
 else
	echo -e "\n${color_red}You entered an invalid input!${color_white}";
 fi
}

### phpOptionSelect() => The part of the prior function that actually makes the changes ###
#    - This is kind of tough for obvious reasons, but ultimately the functionality works
#    - There are minimal checks made on user input; I think all possible accepted inputs are valid, it's just a matter of what is input 
#    - Again, based off of the /etc/cl.selector/php.conf file, so hopefully it is standard or at the very least, valid
## Variables ##
#    - uid = last 2 digits of a user's id ( this is used to determine file location for CloudLinux )
#    - pos = line count for a specific directive ( I use it to find other related valeus )
#    - pos2 = line count also, but used to find separate values than the above
#    - value = custom options once set are placed within a specific file.  The [ selectorctl --print-options ] command seems that it can break depending
#               on the server's php.ini file ( for example if a deprecated variable is still included ), so I wanted to find an alternative
#    - type = type of directive of php option ( list, value, or bool )
#    - desc = comment section; description of the php option
#    - default = the default values of the options ( this gives the user a good idea of an accepted value )
#    - range = only relevant if the type is a list ( I use it as a means to determine accepted user input )
#    - str = a few of the above variables turned into strings to be displayed.  If necessary, they're concatenated
phpOptionSelect()
{
 uid=$(id -u $user | grep -o "..$");
 version=$(selectorctl --user-current --user=$user | awk '{print $1}');
 pos=$(grep "^Directive" /etc/cl.selector/php.conf | grep -n "$1$" | cut -d':' -f1);
 pos2=$(grep -n "$1$" /etc/cl.selector/php.conf | cut -d':' -f1);
 type=$(grep "^Type" /etc/cl.selector/php.conf | head -$pos | tail -1 | awk '{print $3}');
 desc=$(grep "^Comment" /etc/cl.selector/php.conf | head -$pos | tail -1);
 ((pos2+=1));
 default=$(head -$pos2 /etc/cl.selector/php.conf | tail -1 | grep -E "^Default");
 value=$(grep "$1" /var/cagefs/$uid/$user/etc/cl.php.d/alt-php$(echo "$version" | tr -d '.')/alt_php.ini | cut -d'=' -f2);
 ((pos2+=2));
 range=$(head -$pos2 /etc/cl.selector/php.conf | tail -1 | grep -E "^Range");
 str="${color_yellow}$default\n";

 header "$1";
 if [[ $value ]]; then
	str="${str}Current   = ${value}\n";
 fi
 if [[ "$type" == "list" ]]; then
	str="${str}$range\n";
 fi
 str="${str}$desc${color_white}";
 echo -e "$str";

 case "$type" in
	bool)
	 echo -en "\nWhat would you like to change this value to ( 1=ON | 0=OFF ): ";
	 read input;
	 if [[ $(echo "$input" | grep -Ei "^1$|^on$") ]]; then
		selectorctl --add-options=$1:on --version=$version --user=$user;
		echo -e "${color_yellow}\n$1 successfully changed to: ${color_purple}on!${color_white}";
	 elif [[ $(echo "$input" | grep -Ei "^0$|^off$") ]]; then
		selectorctl --add-options=$1:off --version=$version --user=$user;
		echo -e "${color_yellow}\n$1 successfully changed to: ${color_purple}off!${color_white}";
 	 elif [[ "$input" == "" ]]; then
		return 0;
	 else
		echo -e "${color_red}You entered an invalid option!${color_white}";
		return 0;
	 fi;;
	*)
	  echo -ne "\nWhat would you like to change this value to: ";
	  read input;
	  if [[ $(echo "$default" | awk '{print $3}' | grep -E "^\-?[0-9]+$") || $(echo "$range" | tr ',' ' ' | awk '{print $3}' | grep -E "^\-?[0-9]+$") ]]; then
	  	if [[ $(echo "$input" | grep -E "^\-?[0-9]+$") ]]; then 
	  	  selectorctl --add-options=$1:$input --version=$version --user=$user;
		  echo -e "${color_yellow}\n$1 successfully changed to: ${color_purple}$input!${color_white}";
	  	elif [[ "$input" == "" ]]; then
		  return 0;
		else
		  echo -e "${color_red}You entered an invalid option!${color_white}";
		  return 0;
		fi
	  fi
	  if [[ $(echo "$default" | awk '{print $3}' | grep -E "^\-?[0-9]+[s]$") || $(echo "$range" | tr ',' ' ' | awk '{print $3}' | grep -e "^\-?[0-9]+[s]$") ]]; then
		if [[ $(echo "$input" | grep -E "^\-?[0-9]+$") || $(echo "$input" | grep -E "^\-?[0-9]+s$") ]]; then
		  selectorctl --add-options=$1:$input --version=$version --user=$user;
		  echo -e "${color_yellow}\n$1 successfully changed to: ${color_purple}$input!${color_white}";
		elif [[ "$input" == "" ]]; then
		  return 0;
		else
		  echo -e "${color_red}You entered an invalid option!${color_white}";
		  return 0;
		fi
	  fi
	  if [[ $(echo "$default" | awk '{print $3}' | grep -E "^\-?[0-9]+[KMG]$") || $(echo "$range" | tr ',' ' ' | awk '{print $3}' | grep -e "^\-?[0-9]+[KMG]$") ]]; then
		if [[ $(echo "$input" | grep -E "^\-?[0-9]+$") || $(echo "$input" | grep -E "^\-?[0-9]+[KMG]$") ]]; then
		  selectorctl --add-options=$1:$input --version=$version --user=$user;
		  echo -e "${color_yellow}\n$1 successfully chaned to: ${color_purple}$input!${color_white}";
		elif [[ "$input" == "" ]]; then
		  return 0;
		else
		  echo -e "${color_red}You entered an invalid option!${color_white}";
		  return 0;
		fi
	  fi;;
	esac
}

### phpExtensions() => This allows us to turn on/off a user's extensions ###
#    - I'm combining both on and off here rather than separating them
#    - This just checks to see if the extension string is preceded with a + or - and determines whether it is enabled or disabled based off that
## Variables ##
#    - extensions = ALL of the extensions available to the user on the selected php version
phpExtensions()
{
 version=$(selectorctl --user-current --user=$user | awk '{print $1}');
 if [[ $version == "native" ]]; then
        echo -e "\n${color_red}You cannot edit versions while using the native version!${color_white}";
        return 0;
 fi

 while [[ 1 ]]; do
   header "Extensions";
   ii=0;
   extensions=$(selectorctl --list-user-extensions --user=$user --all | tr -d ' ');
   for i in $extensions; do
	if [[ $(echo "$i" | grep -E "^-") ]]; then
	 echo -en "${color_red}";
	else
	 echo -en "${color_yellow}";
	fi
	printf "%s %2s" "$i" && ((ii+=1));
	if [[ $((ii%7)) == 0 ]]; then
	 echo "";
	fi
   done;
   echo -ne "\n${color_white}Which of these extensions would you like to change: ";
   read input;
   if [[ "$input" == "" ]]; then
	break;
   fi
   if [[ $(echo "$input" | grep -E "[-+]") ]]; then
	echo -e "${color_red}Don't need to type out the + or -${color_white}";
	return 0;
   fi
   if [[ $(echo "$extensions" | grep -E "${input}$" 2>&1) ]]; then
	if [[ $(echo "$extensions" | grep -E "^\+${input}$") ]]; then
	  selectorctl --disable-user-extensions=$input --user=$user --version=$version;
	  echo -e "${color_yellow}Attemping to turn $input ${color_purple}off!${color_white}\n";
	else
	  selectorctl --enable-user-extensions=$input --user=$user --version=$version;
	  echo -e "${color_yellow}Attempting to turn $input ${color_purple}on!${color_white}\n";
	fi
   else
	echo -e "${color_red}You entered an invalid option!${color_white}\n";
   fi
 done;
}

## Main Code ##

clear;
user=$1;
preCheck;

# Main menu here:
while [[ 1 ]]; do
        version=$(selectorctl --user-current --user=$user | awk '{print $1}');
        echo -e "\n${color_red}**$user [$version]**${color_white}";
        header "Menu Options";
        echo -e "${color_cyan}[0] Exit\n[1] PHP Extensions \n[2] PHP Options \n[3] PHP Version\n[4] Change User\n${color_white}";
        echo -n "Please select a number: ";
        read input;

        case "$input" in
            0)
             echo -e "\n${color_red}Exiting script...${color_white}\n";
            exit;;
            1)
             phpExtensions;;
            2)
             phpOptions;;
            3)
             phpVersion;;
            4)
             user="";
             getUser;;
        esac
done;
