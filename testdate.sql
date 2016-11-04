SELECT * FROM hms_wh_log WHERE timestamp >= now() - INTERVAL 1 DAY;
SELECT *, timestamp + interval 1 day, verified_date + interval 1 day FROM hms_wh_log;

SELECT FLOOR(HOUR(TIMEDIFF(requested_date, date_verify)) / 24) AS DAYs, HOUR(TIMEDIFF(requested_date, date_verify)) AS HOURs, requested_date, date_verify FROM hms_wh_request_list;

SELECT HOUR(TIMEDIFF(received_date, requested_date)) AS HOURs, received_date, requested_date 
FROM hms_wh_request_list
WHERE HOUR(TIMEDIFF(received_date, requested_date)) > 24;

SELECT request_id, HOUR(TIMEDIFF(requested_date, date_verify)) AS HOURs, requested_date , date_verify
FROM hms_wh_request_list
WHERE HOUR(TIMEDIFF(requested_date, date_verify)) > 24
ORDER BY date_verify DESC;

SELECT request_id, HOUR(TIMEDIFF(requested_date, date_verify)) AS HOURs, requested_date , date_verify
FROM hms_wh_request_list;

SELECT DISTINCT A.reference_id, A.timestamp, A.status, HOUR(TIMEDIFF(A.timestamp, B.timestamp))
FROM
    hms_wh_log A
    INNER JOIN hms_wh_log B
        ON 
		  	A.reference_id = B.reference_id AND
		  	HOUR(TIMEDIFF(A.timestamp, B.timestamp)) < 24
WHERE   
    A.status <> B.status AND
    A.reference_id = B.reference_id AND
    A.reference_id = 6
ORDER BY A.timestamp DESC;


SELECT
    DISTINCT A.reference_id, A.status, A.timestamp, B.timestamp, HOUR(TIMEDIFF(B.timestamp, A.timestamp)) AS HOURs
FROM
    hms_wh_log A 
	 INNER JOIN hms_wh_log B 
	 	ON 
		 	A.reference_id = B.reference_id 
WHERE A.reference_id = B.reference_id AND HOUR(TIMEDIFF(B.timestamp, A.timestamp)) >=0
ORDER BY
    A.reference_id, A.timestamp;

SELECT A.*, date(now()) AS date_now, timediff(date(now()),a.timestamp)
FROM hms_wh_log A
WHERE A.timestamp = (SELECT MAX(B.timestamp)
	                 FROM hms_wh_log B
	                 WHERE A.reference_id = B.reference_id 
	                 	-- 	AND A.status = B.status--
					  		  AND (A.status <> 'Available in Inventory' AND A.status <> 'Closed' AND A.status <> 'Closed. Verified By Supervisor')
							)
ORDER BY reference_id;

    
SELECT A.*, date(now()) AS date_now
FROM hms_wh_log A
WHERE A.timestamp = (SELECT MAX(B.timestamp)
	                 FROM hms_wh_log B
	                 WHERE A.reference_id = B.reference_id 
	                 	-- 	AND A.status = B.status--
					  		  AND (A.status <> 'Available in Inventory' AND A.status <> 'Closed' AND A.status <> 'Closed. Verified By Supervisor')
							)
ORDER BY reference_id;



SELECT A.*, now() AS date_now, DATE_ADD(A.timestamp, INTERVAL 24 HOUR) as timediffff, DATE_SUB(NOW(), INTERVAL 24 HOUR) AS TEMPTIME
FROM hms_wh_log A
WHERE A.timestamp = (SELECT MAX(B.timestamp)
		                 FROM hms_wh_log B
		                 WHERE A.reference_id = B.reference_id
					  		  AND (A.status <> 'Available in Inventory' AND A.status <> 'Closed' AND A.status <> 'Closed. Verified By Supervisor')
							)
							-- AND A.timestamp <= DATE_SUB(NOW(), INTERVAL 24 HOUR) AND DATE_ADD(A.timestamp, INTERVAL 24 HOUR) >= DATE_SUB(NOW(), INTERVAL 24 HOUR) --
ORDER BY reference_id;


SELECT A.*, now() AS date_now, DATE_ADD(A.timestamp, INTERVAL 24 HOUR) as timediffff, DATE_SUB(NOW(), INTERVAL 24 HOUR) AS TEMPTIME
FROM hms_wh_log A
WHERE A.timestamp = (SELECT MAX(B.timestamp)
		                 FROM hms_wh_log B
		                 WHERE A.reference_id = B.reference_id
					  		  AND (A.status <> 'Available in Inventory' AND A.status <> 'Closed' AND A.status <> 'Closed. Verified By Supervisor')
							) AND A.timestamp <= DATE_SUB(NOW(), INTERVAL 24 HOUR) AND DATE_ADD(A.timestamp, INTERVAL 24 HOUR) >= DATE_SUB(NOW(), INTERVAL 24 HOUR)
ORDER BY reference_id;
