<!DOCTYPE html>  
<html lang="en">  
<head>  
    <meta charset="UTF-8">
    <title>Package Report Data</title>
    <style>
    	#customers{font-family:Arial,Helvetica,sans-serif;border-collapse:collapse;width:100%;font-size:14px}#customers td,#customers th{border:1px solid #ddd;padding:8px;font-size:14px}#customers tr:nth-child(even){background-color:#f2f2f2}#customers tr:hover{background-color:#ddd}#customers th{padding-top:6px;padding-bottom:6px;text-align:left;background-color:#c74a1b;color:#fff;font-size:14px}
    </style>
</head> 
<body>  
     <h3>Dear Team, </h3>
     
        <h4> Please verify appointment might be within meeting Timing for " <span style="color:blue"> ${DOC_NAME} ( ${DOC_ID} ) </span> " </h4>
        
		<p>Meeting Timing : ${meetingTime}</p>
        <div>
        <p> *** Appointments before and after 1 hour of meeting time. ***</p>
        <table id="customers">
         <thead>
    		<tr>   			
    			<th>Appointment Dt</th>
    			<th>Duration</th>
    			<th>Category</th>
    		</tr>
    	  </thead>
    	  <tbody>
            <#list appointments as fdata>
            	<tr>			 
 				
 				  <td>${fdata.APPOINTMENT_DT}</td>
 				  <td>${fdata.DURATION}</td>
 				 <td>${fdata.CATEGORY}</td>
 				</tr>
			</#list>
		  </tbody>
		  </table>
        </div>
        <p></p>
		<br>
         <div>
            Thanks & Regards
            <br>
            Team Jiva
            <br>
        </div>
</body>  
</html>