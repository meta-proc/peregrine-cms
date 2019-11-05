cp package.json package.json.original

MDBVUE_PRO_VERSION=5.8.3
MDBVUEURI="git+https:\/\/oauth2:${MDBVUE_TOKEN}@git.mdbootstrap.com\/mdb\/vue\/vu-pro.git#${MDBVUE_PRO_VERSION}"
MDBVUE_VERSION_ORIGINAL=$(grep "mdbvue" "package.json" | sed -e 's/^[ \t]*//' )

echo "laoding mdbv pro"
echo "$MDBVUEURI"

sed -i '.bak' 's/"mdbvue": ".*",/"mdbvue": "'${MDBVUEURI}'",/' package.json
rm package.json.bak

echo "successfully loaded mdbv pro"
