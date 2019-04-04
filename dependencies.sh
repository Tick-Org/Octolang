#!/bin/bash
clear
while true;do
    read -p "install lolcat (for pretty terminal stuff). continue(y/n) " lolcatPrompt
    case $lolcatPrompt in 
        [Yy]* ) sudo gem install lolcat;echo "Finished Installing Lolcat. moving on...";;
        [Nn* ) echo "stopping everything. goodbye";break;;
        * ) echo "stopping everything. goodbye";break;;
    esac
    read -p "install Kotlin ? continue(y/n) " kotlinPrompt
    case $kotlinPrompt in
        [Yy]* ) echo"Installing Kotlin";sudo snap install kotlin --classic;break;
        [Nn]* ) echo "Stopping Bye";break;;
        * ) echo "stopping goodbye";break;;
    esac
    done