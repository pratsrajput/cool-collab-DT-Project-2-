/**
 * Angular JS module and routeprovider configuration 
 */

var app=angular.module("app",['ngRoute','ngCookies'])
app.config(function($routeProvider){
	$routeProvider
	.when('/home',{
		templateUrl:'views/home.html'
	})
	.when('/aboutus',{
		templateUrl:'views/aboutus.html'
	})
	.when('/register',{
		templateUrl:'views/registrationform.html',
		controller:'UserController'
	})
	.when('/login',{
		templateUrl:'views/login.html',
		controller:'UserController'
	})
	.when('/editprofile',{
		templateUrl:'views/updateprofile.html',
		controller:'UserController'
	})
	.when('/savejob',{
		templateUrl:'views/jobform.html',
		controller:'JobController'
	})
	.when('/getalljobs',{
		templateUrl:'views/jobtitles.html',
		controller:'JobController'
	})
	
	.when('/getjobbyid/:id',{
		templateUrl:'views/jobdetail.html',
		controller:'JobDetailController'
	})
	.when('/saveblogpost',{
		templateUrl:'views/blogpostform.html',
		controller:'BlogPostController'
	})
	.when('/getallblogs',{
		templateUrl:'views/listofblogposts.html',
		controller:'BlogPostController'
	})
	
	.when('/getblogpostbyid/:id',{
		templateUrl:'views/blogpostdetail.html',
		controller:'BlogPostDetailController'
	})
	.when('/approveblogpost/:id',{
		templateUrl:'views/blogpostapprovalform.html',
		controller:'BlogPostDetailController'
	})
	
	.when('/uploadprofilepic',{
		templateUrl:'views/profilepicture.html'
	})
	
	.when('/suggesteduserslist',{
		templateUrl:'views/listofsuggestedusers.html',
		controller:'FriendController'
	})
	
	.when('/pendingrequest',{
		templateUrl:'views/listofpendingrequests.html',
		controller:'FriendController'
	})
	
	.when('/getUserDetails/:fromId',{
		templateUrl:'views/userdetails.html',
		controller:'FriendDetailController'
	})
	
	.when('/listoffriends',{
		templateUrl:'views/listoffriends.html',
		controller:'FriendController'
	})
	.when('/chat',{
		templateUrl:'views/chat.html',
		controller:'ChatController'
	})
	.otherwise({
		templateUrl:'views/home.html'
	})
})
app.run(function($rootScope,$cookieStore,UserService,$location){
	
	$rootScope.gotologin=function(){
		$location.path('/login');
	}
		$rootScope.logout=function(){
        UserService.logout().then(function(response){
        	$rootScope.logoutSuccess="Loggedout Successfully.."
        		alert($rootScope.logoutSuccess);
        		delete $rootScope.currentUser
        		$cookieStore.remove("currentUser")
        		$location.path('/login');
        },function(response){
        	$scope.error=response.data
        	$location.path('/login')
        })		
	}
		
		if($rootScope.currentUser==undefined)
			$rootScope.currentUser=$cookieStore.get("currentUser")
			
})


