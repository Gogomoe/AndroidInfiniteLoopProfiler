echarts.init(document.getElementById('{{memory-chart-id}}')).setOption({
    title: {
        text: 'Heap'
    },
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            type: 'cross',
            label: {
                backgroundColor: '#6a7985'
            }
        }
    },
    legend: {
        data: ['Committed', 'Used']
    },
    toolbox: {
        feature: {
            saveAsImage: {}
        }
    },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
    },
    xAxis: [
        {
            type: 'time',
            boundaryGap: false
        }
    ],
    yAxis: [
        {
            type: 'value',
            axisLabel: {
                formatter: function (value) {
                    return numeral(value).format('0b');
                }
            }
        }
    ],
    series: [
        {
            name: 'Committed',
            type: 'line',
            label: {
                show: true,
                formatter: function (params) {
                    return numeral(params.value[1]).format('0.0b');
                }
            },
            areaStyle: {
                color: "rgba(239, 83, 80)"
            },
            data: [
                {{memory-committed}}
            ]
        },
        {
            name: 'Used',
            type: 'line',
            label: {
                show: true,
                formatter: function (params) {
                    return numeral(params.value[1]).format('0.0b');
                }
            },
            areaStyle: {
                color: "rgba(97, 97, 97, 1)"
            },
            data: [
                {{memory-used}}
            ]
        }
    ]
});