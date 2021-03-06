SELECT *
INTO dbo.StringMap
FROM
(
	SELECT 'AvailabilityTbl' TableName, 'DayOfWeek' ColumnName, 1 [Key], 'Sunday' [Value] UNION ALL
	SELECT 'AvailabilityTbl', 'DayOfWeek', 2, 'Monday' UNION ALL
	SELECT 'AvailabilityTbl', 'DayOfWeek', 3, 'Tuesday' UNION ALL
	SELECT 'AvailabilityTbl', 'DayOfWeek', 4, 'Wednesday' UNION ALL
	SELECT 'AvailabilityTbl', 'DayOfWeek', 5, 'Thursday' UNION ALL
	SELECT 'AvailabilityTbl', 'DayOfWeek', 6, 'Friday' UNION ALL
	SELECT 'AvailabilityTbl', 'DayOfWeek', 7, 'Satday' UNION ALL
	SELECT 'Schedule', 'ShiftStatus', 0, 'ShiftDropped' UNION ALL
	SELECT 'Schedule', 'ShiftStatus', 1, 'Active' UNION ALL
	SELECT 'Employee', 'JobType', 0, 'Manager' UNION ALL
	SELECT 'Employee', 'JobType', 1, 'Employee' UNION ALL
	SELECT 'Employee', 'Status', 0, 'Active' UNION ALL
	SELECT 'Employee', 'Status', 1, 'Deleted' UNION ALL
	SELECT 'Request', 'RequestType', 1, 'DropShift' UNION ALL
	SELECT 'Request', 'RequestType', 2, 'PickupShift' UNION ALL
	SELECT 'Request', 'RequestType', 3, 'SwapShift' UNION ALL
	SELECT 'Request', 'RequestType', 4, 'AcceptAvailability' UNION ALL
	SELECT 'Request', 'RequestStatus', 0, 'Pending' UNION ALL
	SELECT 'Request', 'RequestStatus', 1, 'Approved' UNION ALL
	SELECT 'Request', 'RequestStatus', 2, 'Denied'
) t