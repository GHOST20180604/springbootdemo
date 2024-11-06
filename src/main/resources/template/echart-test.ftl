<html>

<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>ECharts Demo</title>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/echarts/5.2.2/echarts.min.js"
            integrity="sha512-ivdGNkeO+FTZH5ZoVC4gS4ovGSiWc+6v60/hvHkccaMN2BXchfKdvEZtviy5L4xSpF8NPsfS0EVNSGf+EsUdxA=="
            crossorigin="anonymous" referrerpolicy="no-referrer"></script>
    <style>
        body {
            margin: 0;
            display: flex;
            flex-direction: row;
            justify-content: center;
        }
        #display-container {
            width: 600px;
            height: 600px;
            border: 2px solid black;
        }
    </style>
</head>

<body>
<div id="container">

    <div id="display-container">
    </div>
</div>

<script type="text/javascript">

    var chart = echarts.init(document.getElementById("display-container"));
    var option = {
        "animation": false,

        "xAxis": {
            "type": "category",
            "axisTick": {
                "alignWithLabel": true
            },
            "data": ${xAxisData!'[]'}
        },
        "yAxis": {
            "type": "value"
        },
        "tooltip": {
            "axisPointer": {
                "type": "shadow"
            },
            "trigger": "axis"
        },
        "series": [
            {
                "type": "bar",
                "name": "Direct",
                "data": ${yAxisData!'[]'},
                "barWidth": "60%"
            }
        ]
    }
    chart.setOption(option);
</script>
</body>

</html>