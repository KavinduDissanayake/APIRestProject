<?php

  header('Access-Control-Allow-Origin: *');
  header('Content-Type: application/json');
  header('Access-Control-Allow-Methods: POST');
  header('Access-Control-Allow-Headers: Access-Control-Allow-Headers, Content-Type, Access-Control-Allow-Methods, Authorization, X-Requested-With');

  //initaliaztion our api 
  include_once('../core/initialize.php');


  //instantiate cdc 


  $cdc = new  CDC($db);


  //blog cdc  quary
  $result =$cdc -> read();

  //get the row count


  $num =  $result->rowCount();

  if($num >0){

    $cdc_arr= array();
    $cdc_arr['cdc_data']=array();

    while($row =$result ->fetch(PDO::FETCH_ASSOC)){
        extract($row);
        $cdc_item= array(

            'uid' =>$uid,
            'nic' =>$nic,
            'name' =>$name,
            'email' =>$email,
            'dob' =>$dob,
            'dob' =>$dob,
            'no' =>$no,
            'street' =>$street,
            'city' =>$city,
            'phone_num' =>$phone_num,
            'status' =>$status,
            'current_lat' =>$current_lat,
            'current_lon' =>$current_lon,
            'img_path' =>$img_path,

        );
    //    array_push($cdc_arr['cdc_data'],$cdc_item);
        array_push($cdc_arr['cdc_data'],$cdc_item);

    }

    //convet to json and output

    echo json_encode($cdc_arr);

    

  }else{


    echo json_encode(array('message'=>'no cdc found'));


  }




?>