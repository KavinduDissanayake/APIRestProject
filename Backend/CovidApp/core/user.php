<?php


class User {

    private $con;
    private $table='user';
    private $base_url='http://192.168.1.10/CovidApp/';

    //post properties 

    public $uid;
    public $nic;
    public $name;
    public $email;
    public $dob;
    public $no;
    public $street;
    public $city;
    public $phone_num;
    public $status;
    public $current_lat;
    public $current_lon;
    public $img_path;
    public $user_role;
    public $encodedImage;
    public $uploade_url;


    //counstructor with db connection 
    public function __construct($db) {
        $this->con = $db;
    }

    //getting data from datbase 
    
    public function read() {

    $query = "SELECT `uid`,`nic`,`name`,`email`,`dob`,`no`,`street`,`city`,`phone_num`,`status`,`img_path`,`lat` as 'current_lat',`lon` as  'current_lon' 
    FROM $this->table
    WHERE `user_role`='Citizens';";

   //echo $query;
    $stmt = $this -> con -> prepare($query);

    $stmt->execute();

    return $stmt;
}




  
public function read_user_data() {
    $query = "SELECT `uid`,`nic`,`name`,`email`,`dob`,`no`,`street`,`city`,`phone_num`,`status`,`img_path`,`lat` as 'current_lat',`lon` as  'current_lon' 
    FROM $this->table
    WHERE `user_role`='Citizens' 
    AND uid= ? LIMIT 1  ;";

   //echo $query;

   //prapre statement
    $stmt = $this -> con -> prepare($query);

    //binding parm
    $stmt->bindParam(1,$this->uid);

    //execute the quary
    $stmt->execute();

    $row = $stmt->fetch(PDO::FETCH_ASSOC);


    $this->uid = $row['uid'];
    $this->nic = $row['nic'];
    $this->name = $row['name'];
    $this->email = $row['email'];
    $this->dob = $row['dob'];
    $this->no = $row['no'];
    $this->street = $row['street'];
    $this->city = $row['city'];
    $this->phone_num = $row['phone_num'];
    $this->status = $row['status'];
    $this->current_lat = $row['current_lat'];
    $this->current_lon = $row['current_lon'];
    $this->img_path = $row['img_path'];



    return $stmt;
}




 
public function read_user_data_for_phi() {
    $query = "SELECT `uid`,`nic`,`name`,`email`,`dob`,`no`,`street`,`city`,`phone_num`,`status`,`img_path`,`lat` as 'current_lat',`lon` as  'current_lon' 
    FROM $this->table
    WHERE `user_role`='Citizens' 
    AND nic= ? LIMIT 1  ;";

   //echo $query;

   //prapre statement
    $stmt = $this -> con -> prepare($query);

    //binding parm
    $stmt->bindParam(1,$this->nic);

    //execute the quary
    $stmt->execute();

    $row = $stmt->fetch(PDO::FETCH_ASSOC);


    $this->uid = $row['uid'];
    $this->nic = $row['nic'];
    $this->name = $row['name'];
    $this->email = $row['email'];
    $this->dob = $row['dob'];
    $this->no = $row['no'];
    $this->street = $row['street'];
    $this->city = $row['city'];
    $this->phone_num = $row['phone_num'];
    $this->status = $row['status'];
    $this->current_lat = $row['current_lat'];
    $this->current_lon = $row['current_lon'];
    $this->img_path = $row['img_path'];



    return $stmt;
}



public function user_regsiter(){

    $query="INSERT INTO `user` (`uid`, `nic`,`name`, `email`, `dob`, `no`, `street`, `city`, `phone_num`, `password`, `status`, `user_role`, `img_path`, `lat`, `lon`)
     VALUES
      (NULL, :nic,:name, :email, :dob,:no, :street, :city, :phone_num,:password,:status,:user_role, '', '0', '0');";


            $stmt = $this -> con -> prepare($query);

                //clean data
                $this->nic = htmlspecialchars(strip_tags($this->nic));
                $this->name = htmlspecialchars(strip_tags($this->name));
                $this->email = htmlspecialchars(strip_tags($this->email));
                $this->dob = htmlspecialchars(strip_tags($this->dob));
                $this->no = htmlspecialchars(strip_tags($this->no));
                $this->street = htmlspecialchars(strip_tags($this->street));
                $this->city = htmlspecialchars(strip_tags($this->city));
                $this->phone_num = htmlspecialchars(strip_tags($this->phone_num));
                $this->password = htmlspecialchars(strip_tags($this->password));
                $this->status = htmlspecialchars(strip_tags($this->status));
                $this->user_role = htmlspecialchars(strip_tags($this->user_role));
              



                //binding parameters 
                $stmt->bindParam(':nic', $this->nic);
                $stmt->bindParam(':name', $this->name);
                $stmt->bindParam(':email', $this->email);
                $stmt->bindParam(':dob', $this->dob);
                $stmt->bindParam(':no', $this->no);
                $stmt->bindParam(':street', $this->street);
                $stmt->bindParam(':city', $this->city);
                $stmt->bindParam(':phone_num', $this->phone_num);
                $stmt->bindParam(':password', $this->password);
                $stmt->bindParam(':status', $this->status);
                $stmt->bindParam(':user_role', $this->user_role);
               

                    try {

                        if($stmt->execute()) { return true; }

                        }catch(PDOException $e){      
                            
                            // echo $e->getMessage();
                            return false;  
                        }      
                        
          
                        
          

}







 
public function user_auth() {

   
    $query = "SELECT `uid`,`nic`,`user_role` 
    FROM $this->table
    WHERE `user_role`='Citizens' 
    AND (nic= :nic AND `password`=:password )LIMIT 1  ;";
   //echo $query;
    $stmt = $this -> con -> prepare($query);



       //binding parm
       $stmt->bindParam(':nic',$this->nic);
       $stmt->bindParam(':password',$this->password);

       
    $stmt->execute();

    return $stmt;
}





//update user info funcation 

public function user_update_info(){

    $query="UPDATE $this->table SET `name`= :name,`email`=:email,`dob`=:dob,`no`=:no,`street`=:street,`city`=:city,`phone_num`=:phone_num 
    WHERE `nic`= :nic";


            $stmt = $this -> con -> prepare($query);

                //clean data
                $this->nic = htmlspecialchars(strip_tags($this->nic));
                $this->name = htmlspecialchars(strip_tags($this->name));
                $this->email = htmlspecialchars(strip_tags($this->email));
                $this->dob = htmlspecialchars(strip_tags($this->dob));
                $this->no = htmlspecialchars(strip_tags($this->no));
                $this->street = htmlspecialchars(strip_tags($this->street));
                $this->city = htmlspecialchars(strip_tags($this->city));
                
              



                //binding parameters 
                $stmt->bindParam(':nic', $this->nic);
                $stmt->bindParam(':name', $this->name);
                $stmt->bindParam(':email', $this->email);
                $stmt->bindParam(':dob', $this->dob);
                $stmt->bindParam(':no', $this->no);
                $stmt->bindParam(':street', $this->street);
                $stmt->bindParam(':city', $this->city);
                $stmt->bindParam(':phone_num', $this->phone_num);
               
               

                    try {

                        if($stmt->execute()) { return true; }

                        }catch(PDOException $e){      
                            
                            // echo $e->getMessage();
                            return false;  
                        }      
                        
          
                        
          

}





//Upload  Profile Image funcation 

public function user_upload_profile_image(){



	$imageLocation = "../Images/User/$this->uid.jpg";
    $imagpath="Images/User/$this->uid.jpg";
	$this->uploade_url=$this->base_url.$imagpath;

    if(file_put_contents($imageLocation, base64_decode($this->encodedImage))){

     


                $query="UPDATE $this->table SET `img_path`=:img_path WHERE `uid`=:uid";


            $stmt = $this -> con -> prepare($query);

                //clean data
                $this->uid = htmlspecialchars(strip_tags($this->uid));
                $this->img_path = htmlspecialchars(strip_tags($this->img_path));
            
                
            


                //binding parameters 
                $stmt->bindParam(':uid', $this->uid);
                $stmt->bindParam(':img_path', $this->uploade_url);
               
               

                    try {

                        if($stmt->execute()) { return true; }

                        }catch(PDOException $e){      
                            
                            // echo $e->getMessage();
                            return false;  
                        }      

        }else{

            return false;
        }

                    
                        
          
                        
          

}







//update user info funcation 

public function user_update_current_location(){

    $query="UPDATE `user` SET `lat`=:lat,`lon`=:lon WHERE `nic`=:nic";


            $stmt = $this -> con -> prepare($query);

                //clean data
                $this->nic = htmlspecialchars(strip_tags($this->nic));
                $this->current_lat = htmlspecialchars(strip_tags($this->current_lat));
                $this->email = htmlspecialchars(strip_tags($this->email));
       
              



                //binding parameters 
                $stmt->bindParam(':nic', $this->nic);
                $stmt->bindParam(':lat', $this->current_lat);
                $stmt->bindParam(':lon', $this->current_lon);
        
               
               

                    try {

                        if($stmt->execute()) { return true; }

                        }catch(PDOException $e){      
                            
                            // echo $e->getMessage();
                            return false;  
                        }      
                        
          
                        
          

}


}

?>