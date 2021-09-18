echo "Start running java 'el-service' application in container"

echo "Init environment"
echo "User home is $HOME"
mkdir "$HOME"/esl
export LOCAL_SOURCE_DIR="$HOME"/esl
echo "Set local source directory env '$LOCAL_SOURCE_DIR' is performed"

echo "Run application"
java -jar QJ-1.0-SNAPSHOT-runner.jar