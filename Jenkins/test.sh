hogapid=$(docker ps -a -q --filter ancestor=hogajama --format="{{.ID}}")

if [ ! -z "$hogapid" ]; then
        docker rm $(docker stop $hogapid)
fi

docker run -p 8080:8080 -p 9990:9990 -d hogajama
