//docker build config/docker/box -t 172.29.23.100:5000/marm-spring-box
//docker build config/docker/postgres -t 172.29.23.100:5000/marm-spring-postgres
//docker build config/docker/mongo -t 172.29.23.100:5000/marm-spring-mongo
//docker push 172.29.23.100:5000/marm-spring-box
//docker push 172.29.23.100:5000/marm-spring-postgres
//docker push 172.29.23.100:5000/marm-spring-mongo
//
//export DOCKER_HOST=tcp://172.29.23.101:2375
//docker-compose up -d --no-deps --build <service_name>
//
//docker-compose -f config/docker/docker-compose.yml stop
//docker-compose -f config/docker/docker-compose.yml rm -f
//docker rmi 172.29.23.100:5000/marm-spring-box 172.29.23.100:5000/marm-spring-postgres 172.29.23.100:5000/marm-spring-mongo
//docker-compose -f config/docker/docker-compose.yml up -d
//
//DOCKER_DB_NAME="$(docker-compose -f config/docker/docker-compose.yml ps -q postgres)"
//DB_HOSTNAME=marm-spring-postgres
//DB_USER=postgres
//LOCAL_DUMP_PATH="${WORKSPACE}/config/docker/postgres/dump/pgdump.backup"
//
//docker exec -i "${DOCKER_DB_NAME}" pg_restore -C --clean --no-acl --no-owner -U "${DB_USER}" -d "${DB_HOSTNAME}" < "${LOCAL_DUMP_PATH}"