DELETE FROM inquiry;

INSERT INTO inquiry (id, first_name, last_name, email, message, enabled, created_datetime) VALUES
(1, 'John', 'Doe', 'john.doe@example.com', 'I am interested in your services. Could you provide more details?', true, '2025-08-01 10:15:30'),
(2, 'Jane', 'Smith', 'jane.smith@example.com', 'Please call me regarding the inquiry I sent last week.', true, '2025-08-02 09:42:10'),
(3, 'Michael', 'Johnson', 'michael.j@example.com', 'Can I schedule a demo for next Monday?', true, '2025-08-03 14:20:55'),
(4, 'Emily', 'Davis', 'emily.davis@example.com', 'I am having issues logging into my account. Please assist.', false, '2025-08-04 16:05:12'),
(5, 'David', 'Wilson', 'david.wilson@example.com', 'Could you send me the updated pricing sheet?', true, '2025-08-05 11:30:22'),
(6, 'Sarah', 'Miller', 'sarah.miller@example.com', 'I am looking for partnership opportunities. Kindly connect.', true, '2025-08-06 13:50:45'),
(7, 'Chris', 'Brown', 'chris.brown@example.com', 'I forgot my password. Can you help me reset it?', false, '2025-08-07 08:25:40'),
(8, 'Laura', 'Taylor', 'laura.taylor@example.com', 'Do you offer bulk discounts for corporate clients?', true, '2025-08-08 17:10:33'),
(9, 'Daniel', 'Anderson', 'daniel.anderson@example.com', 'I would like to unsubscribe from your newsletter.', true, '2025-08-09 12:05:50'),
(10, 'Sophia', 'Thomas', 'sophia.thomas@example.com', 'Your customer support was excellent. Thank you!', true, '2025-08-10 19:45:22'),
(11, 'James', 'Moore', 'james.moore@example.com', 'Is your service available in Canada?', true, '2025-08-11 07:20:13'),
(12, 'Olivia', 'Martin', 'olivia.martin@example.com', 'Please share the case study you mentioned earlier.', true, '2025-08-12 15:35:11'),
(13, 'Ethan', 'Jackson', 'ethan.jackson@example.com', 'Can I change my subscription plan mid-term?', false, '2025-08-13 09:10:27'),
(14, 'Ava', 'White', 'ava.white@example.com', 'Looking forward to the product launch. Please keep me updated.', true, '2025-08-14 20:55:44'),
(15, 'Matthew', 'Harris', 'matthew.harris@example.com', 'I tried to pay but the transaction failed. Please check.', true, '2025-08-15 18:22:39'),
(16, 'Isabella', 'Clark', 'isabella.clark@example.com', 'Could you send me the invoice for last month?', true, '2025-08-16 14:40:00'),
(17, 'Benjamin', 'Lewis', 'benjamin.lewis@example.com', 'I want to change my registered email address.', true, '2025-08-17 11:11:11'),
(18, 'Mia', 'Walker', 'mia.walker@example.com', 'What are the supported payment methods?', true, '2025-08-18 08:45:28'),
(19, 'Alexander', 'Hall', 'alex.hall@example.com', 'My order has not arrived yet. Can you track it?', false, '2025-08-19 10:25:36'),
(20, 'Charlotte', 'Allen', 'charlotte.allen@example.com', 'Thank you for the quick response to my last query.', true, '2025-08-20 16:15:19'),
(21, 'Henry', 'Young', 'henry.young@example.com', 'Can I speak with a sales representative tomorrow?', true, '2025-08-21 09:00:42'),
(22, 'Amelia', 'King', 'amelia.king@example.com', 'Is there a free trial available for new users?', true, '2025-08-22 14:55:07'),
(23, 'William', 'Scott', 'william.scott@example.com', 'I would like to delete my account permanently.', false, '2025-08-23 12:12:12'),
(24, 'Harper', 'Green', 'harper.green@example.com', 'Can you add me to the beta program mailing list?', true, '2025-08-24 19:45:55'),
(25, 'Lucas', 'Adams', 'lucas.adams@example.com', 'Do you provide training sessions for enterprise customers?', true, '2025-08-25 07:35:29');
COMMIT;