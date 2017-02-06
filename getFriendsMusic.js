var friends =API.friends.get({"count":20,"fields":"first_name,last_name","v":"5.44"}).items;
var albums =[];
var countFriends = 0;
while(countFriends<friends.length){
    var targetUser = friends[countFriends];
    var album = {"owner":{},"tracks":[]};
    album.owner = targetUser;
    var music = API.audio.get({"owner_id":targetUser.id,"count":10}).items;
    if(music.length>2&&albums.length<10){
    var countAudios = 0;
    while(countAudios<music.length){
    album.tracks.push(music[countAudios]);
    countAudios = countAudios + 1;
    }
    albums.push(album);
    }
countFriends = countFriends+1;
}
return albums;