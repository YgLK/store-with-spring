<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <title>Login</title>
    <!-- Bootstrap core CSS -->
    <link href="assets/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">

    <meta name="theme-color" content="#563d7c">


    <style>
        .button {
            background-color: #4CAF50; /* Green */
            border: none;
            color: white;
            padding: 15px 32px;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            font-size: 16px;
            transition-duration: 0.4s;
        }

        .button:hover {
            background-color: #4CAF50; /* Green */
            color: white;
        }

        .error {
            color: #ff0000;
        }

        .errorblock {
            color: #000;
            background-color: #ffEEEE;
            border: 3px solid #ff0000;
            padding: 8px;
            margin: 16px;
        }
    </style>
    <!-- Custom styles for this template -->
    <link href="navbar-top.css" rel="stylesheet">
</head>
<body>
<nav class="navbar navbar-expand-md navbar-dark bg-dark mb-4">
        <a class="navbar-brand" href="/store/menu">Get Started</a>
</nav>

<div class="container">
    <div>
        <h1>Order has been successfully placed.</h1>
    </div>
    <div>
        <table>
            <tr>
                <td>
                    <a href="product-catalog" class="btn btn-lg btn-primary btn-block" >Create new order</a><br>
                </td>
            </tr>
            <tr>
                <td>
                    <a href="menu" class="btn btn-lg btn-primary btn-block">Go to menu</a><br>
                </td>
            </tr>
            <tr>
                <td>
                    <a href="show-orders" class="btn btn-lg btn-primary btn-block">All placed orders</a><br>
                </td>
            </tr>
        </table>
    </div>
</div>
</body>
</html>
