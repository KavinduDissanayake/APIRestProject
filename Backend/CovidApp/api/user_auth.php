<?php

  header('Access-Control-Allow-Origin: *');
  header('Content-Type: application/json');
  header('Access-Control-Allow-Methods: POST');
  header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Content-Type, Access-Control-Allow-Methods, Authorization, X-Requested-With');

  //initaliaztion our api 
  include_once('../core/initialize.php');


  //instantiate user 

 //instantiate user 


 $user = new  User($db);





 $user->nic = isset($_POST['nic']) ? $_POST['nic'] : die();
 //use password encryption 
 $user->password = isset($_POST['password']) ? md5($_POST['password']) : die();



 //blog user  quary
 $result =$user -> user_auth();

 //get the row count


 $num =  $result->rowCount();

 if($num >0){

   $user_arr= array();
//    $user_arr['user_data']=array();

   while($row =$result ->fetch(PDO::FETCH_ASSOC)){
       extract($row);
       $user_arr= array(

           'message' =>'Authentication Success',
           'uid' =>$uid,
           'nic' =>$nic,
           'user_role' =>$user_role,
       
       );
  

   }

   //convet to json and output
   echo json_encode($user_arr);
 

 }else{

   echo json_encode(array('message'=>'Authentication failed'));


 }

  




?>