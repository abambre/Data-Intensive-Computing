#!/bin/bash


ssh sbhatia3@152.46.20.64 <<'ENDSSH'

/usr/local/hadoop/sbin/hadoop-daemon.sh start datanode
if [ "$?" -ne 0 ]
then
    echo "Error while launching data node"
    exit 1;
fi

/usr/local/hadoop/sbin/yarn-daemon.sh start nodemanager

if [ "$?" -ne 0 ]
then
    echo "Error while launching Node manager"
    exit 1;
fi

ENDSSH



