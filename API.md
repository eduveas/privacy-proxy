Privacy-Proxy API
=================

This document lists the endpoints of the privacy-proxy as well as the data formats of the inputs and outputs
Endpoints are given as relative URLs.


Endpoints
---------

###api/v0/recommend/rewrite

 This endpoint requests a given user context (trace) to be mapped into an array of weighted search terms from an user context.
 It is used by the plugin prototype to visualize the recommendation query. 

  - #####INPUT :
a [trace](#trace)

 
  - #####OUTPUT :

<pre><code>{
    "content": [
        {
            "term": "%TERM%",
            "score":%SCORE%
        },
        {
            "term":"%TERM%",
            "score":%SCORE%
        }
    ],
  "ponderatedTopics": [
        {
            "term":"%TOPIC%",
            "value":%VALUE%
        },
        {
            "term":"%TOPIC%",
            "value":%VALUE%
        }
    ]
 }</code></pre>


###api/v0/recommend/fetch

  This endpoint allows obtaining recommendations for a given user context (trace).

  - #####INPUT : 
  	a [trace](#trace)

  - #####OUTPUT :
  	an html list (`<ul><li> [...] </li></ul>`)


###api/v0/privacy/trace

  This endpoint receives one trace (element of the browsing history) and stores it.
 
  - ##### INPUT:
   A [trace](#trace)

  - ##### OUTPUT:
 Acknowledgement that the trace has been stored.

<pre><code>{
    "ok": true,
    "_index": "privacy",
    "_type": "trace",
    "_id": "%id assigned to the sent trace by Elasticsearch%",
    "_version": %number of times the trace have been updated%`
}
</code></pre>


###api/v0/user/profile

 This endpoint allows the retrieve the currently stored user profile.
 
  - <h5>INPUT:</h5>
<pre><code>{
  "_id": "%ID%"
}
</code></pre>
 
  - <h5>OUTPUT:</h5>
<pre><code>{
    "id": "%ID%",
    "values":{
        %rest of the <a href='#user-profile'>user profile</a>%
    }
 }
</code></pre>


###api/v0/user/traces

  This endpoint allows fetches a user's traces for either a given plugin installation id, a given user id or both, depending on which are specified.

  - <h5>INPUT:</h5>
<pre><code>{
    "pluginId": %plugin's id%";
    "userId": "%user's id%",
    "environnement": "%environnement (either "work" or "home")%"
 }</code></pre>
 **NOTE:** If both pluginId and userId fields are filled, the results returned will be the traces which are for the given user *and* obtained through the given pluginId.
 
  - <h5>OUTPUT:</h5>
   an Elasticsearch list of [traces](#trace), ordered from latest to oldest, maximum 50	
<pre><code>{
    took: 4,
    timed_out: false,
    _shards: {
        total: 5,
        successful: 5,
        failed: 0
    }
    hits:
    {
        total: 50,
        max_score: 1,
        hits: [
           % the traces %
        ]
    }
}</code></pre>
   		 

###api/v0/user/data
  This endpoint allows to push an updated user profile

  - ##### INPUT:
   the [user profile](#user-profile)
   
  - ##### OUTPUT:
  a response from Elasticsearch :
<pre><code>{
    "ok": true,
    "_index": "users",
    "_type": "data",
    "_id": "%user id%"
    "_version": %number of times the user have been updated%
 }</code></pre>
  

###api/v0/user/privacy_settings
  This endpoint allows to push an updated user profile directly without merging it with other profiles first. It is used mainly to store the user's privacy settings.

  - ##### INPUT:
   the [user profile](#user-profile)
  
  - ##### OUTPUT:
   %user id% 
		 

###api/v0/user/exists
 This endpoint checks if given username or email currently exist

  - <h5> INPUT: </h5>
JSON with the following structure
<pre><code>{
    "user_email": "%user email%",
    "user_id": "%user id%"
}</code></pre>

**NOTE:** Only one of the fields is necessary
 
  - <h5>OUTPUT:</h5> 
<pre><code>{
    "hits": %number%
 }</code></pre>

###api/v0/user/authenticate
 Endpoint allowing to obtain a user's id from his login informations ( username/ email and password )

  - <h5>INPUT:</h5>
<pre><code>{
    "term": 
      {
        "data.%email | username%": "%email | username%"
      },
}
{
    "term": 
        {
        "data.password": "%password%"
        }
}
}</code></pre>
**NOTE:** You can either use the email or the username
 
  - <h5>OUTPUT:</h5>
<pre><code>{
    "loginValid": "% 1 if the credentials were correct, 0 if they weren't %",
    "id": "%user id%",
    "username": %username%
}</code></pre>
   

###api/v0/connect/mendeley/init
 Endpoint to initiate Mendeley [OAuth authentication](http://apidocs.mendeley.com/home/authentication)

  - #####  INPUT : nothing
  
  - #####  OUTPUT : 
    - HEADERS : 
	oauth`_`token, oauth`_`token_secret
 	
        NOTE : these credentials are given by Mendeley and are needed to pursue the oauth process ( redirecting the user to mendeley's authorization page )


###api/v0/connect/mendeley/validate
 Endpoint to finalize the Mendeley oauth authentication, importing the user's Mendeley profile to the database and eventualy merging it with an existing eexcess [profile](#user-profile)

  - INPUT :
    - HEADERS :
 		user`_`id (optional, is used to merge the mendeley profile with an existing eexcess profile),
 		oauth`_`token,
 		oauth`_`token`_`secret,
 		ouath`_`verifier,
 
    - OUTPUT : 
<pre><code>
{
    "ok": true,
    "_index": "users",
    "_type": "data",
    "_id": "%user id%",
    "_version": %number of times the user have been updated%
}
</code></pre>





Data formats
------------

<h3 id='profile'>USER PROFILE</h3>
<pre><code>
{
    "username": "johndoe",
    "email": "john.doe@email.com",
    "password": "223252646cffg9dac77d03e16802078c",
    "privacy": {
        "email": "1",
        "gender": "0",
        "title": "0",
        "traces": "2",
        "geoloc": "0",
        "birthdate": "2",
        "address": "2",
    },
    "title": "Mister",
    "lastname": "Doe",
    "firstname": "John",
    "gender": "Male",
    "birthdate": "1990-04-06",
    "address": {
        "street": "13 rue du Chene",
        "postalcode": "69100",
        "city": "Villeurbanne",
        "country": "France",
        "region": "Rhone-Alpes",
        "district": "Arrondissement de Lyon"
    },
    topics: [
    {
        "label":  "privacy preservation",
        "env": "all",
        "source": "eexcess"
    },
    {
        "label: artificial intelligence",
        "env: work",
        "source: mendeley"
    },
    {
        "label": "cookies",
        "env": "home",
        "source": "eexcess"
    }
    ]
}
</code></pre>

<h3 id='trace'>TRACE</h3>

<pre><code>
{
    "user": {
        "user_id": "G7-Eh2EHRj6trSUa7rbbJw",
        "environnement": "home"
    },
    "plugin": {
        "version": "1.00",
        "uuid": "8437409A-B015-4F86-A3FA-05D911CC58F2"
    },
    "temporal": {
        "begin": "2013-09-11T17:00:35Z",
        "end": "2013-09-11T17:00:37Z"
    },
    "events": {
        "begin": "focus",
        "end": "unload"
    },
    "document": {
        "url": "http:fr.wikipedia.org/wiki/Xkcd",
        "title": "xkcd - Wikipédia"
    },
    "geolocation": {
        "country": "FR",
        "region": "Rhone-Alpes",
        "district": "Arrondissement de Lyon",
        "place": "Villeurbanne",
        "coord": "lat=45.771944,lng=4.890171"
    }
}
</code></pre>
