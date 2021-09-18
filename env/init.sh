# Creating directory for and set env LOCAL_SOURCE_DIR for local development
echo "User home is $HOME"
mkdir "$HOME"/esl
export LOCAL_SOURCE_DIR="$HOME"/esl
echo "Set local source directory env '$LOCAL_SOURCE_DIR' is performed"

# Copy test data to LOCAL_SOURCE_DIR
echo "Content before copying of test data"
ls -l "$LOCAL_SOURCE_DIR"
cp ../src/test/resources/pdf/*.pdf "$LOCAL_SOURCE_DIR"
echo "Content after copying of test data"
ls -l "$LOCAL_SOURCE_DIR"