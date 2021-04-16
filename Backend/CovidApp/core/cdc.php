
<?php

class CDC {

    private $con;
    private $table='user';

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
    public $history_lat;
    public $history_lon;
    public $img_path;
    public $user_role;
    public $encodedImage;
    public $uploade_url;

  //counstructor with db connection 
  public function __construct($db) {
    $this->con = $db;
}






//DELETE	/citizens/:nid 

public function citizens_delete(){

    $query="DELETE  `user`,citizens_hostory From 
    user
    INNER join citizens_hostory on user.uid= citizens_hostory.uid
    WHERE user.nic=:nic and user.status='Deceased'";


            $stmt = $this -> con -> prepare($query);

                //clean data
                $this->nic = htmlspecialchars(strip_tags($this->nic));
             
    

                //binding parameters 
                $stmt->bindParam(':nic', $this->nic);
               
        
               
               

                    try {

                        if($stmt->execute()) { return true; }

                        }catch(PDOException $e){      
                            
                            // echo $e->getMessage();
                            return false;  
                        }      
                        
          
                        
          

}





}


?>