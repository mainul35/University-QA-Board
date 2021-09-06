CONTAINER_NAMES=('qaboard_db' 'qa_board')
for i in "${CONTAINER_NAMES[@]}"
do
	CID=$(docker ps -q -f status=running -f name=^/$i$)
  if [ ! "${CID}" ]; then
    docker rm container $i
  else
    docker stop $CID
    docker rm container $CID
  fi
  unset CID
done

IMAGE_NAME='mainul35/university-qa-board'
if test ! -z "$(docker images -q ${IMAGE_NAME})"; then
  echo "Removing image..."
  sudo docker image rm $IMAGE_NAME
fi
