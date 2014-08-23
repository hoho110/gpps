
    function ForYieldMovements(t1, t2, t3, t4, dates) {
        var line = new RGraph.Line('cvs', GetNumList(t1), GetNumList(t2), GetNumList(t3), GetNumList(t4));
        line.Set('background.grid.vlines', true);
        line.Set('background.color', '#fffde7');
        line.Set('chart.labels', dates);
        line.Set('linewidth', 2);
        line.Set('tooltips', GetStrList(t1), GetStrList(t2), GetStrList(t3), GetStrList(t4));
        line.Set('chart.colors', GetColors(4));
        line.Set('chart.ymax', GetMaxInList([GetNumList(t1), GetNumList(t2), GetNumList(t3), GetNumList(t4)], 0.5));
        line.Set('chart.ymin', GetMinInList([GetNumList(t1), GetNumList(t2), GetNumList(t3), GetNumList(t4)], 0.5));
        line.Set('scale.decimals', 1);
        line.Set('hmargin', 30);
        line.Set('gutter.bottom', 30);
        line.Set('chart.tickmarks', 'circle');
        
        
        var ks = new Array();
        ks.push('1月以内');
        ks.push('1-3个月');
        ks.push('3-6个月');
        ks.push('6-12个月');
        line.Set('chart.key', ks);
        line.Set('chart.key.colors', GetColors(4));
        line.Set('chart.key.position.y', 5);
        line.Set('chart.key.position', 'gutter');
        line.Set('text.size', 8);
        if (typeof (Worker) !== "undefined") {
            RGraph.Redraw();
            RGraph.Effects.Line.jQuery.UnfoldFromCenterTrace(line, { 'duration': 1000 });
        } else {
            line.Draw();
        }
    }

   
    //将集合的每个成员转变成number类型返回
    function GetNumList(list) {
        for (var i = 0; i < list.length; i++) {
            list[i] = list[i] * 1;
        }
        return list;
    }
//将集合的每个成员转变成string类型返回
    function GetStrList(list, Unit) {
        if (Unit == null) {
            Unit = "";
        }
        var newlist = [];
        for (var i = 0; i < list.length; i++) {
            if (typeof (list[i]) == "object") {
                newlist[i] = GetStrList(list[i]);
            }
            else {
                newlist[i] = list[i].toString() + Unit;
            }
        }
        return newlist;
    };

    
    
    function GetMaxInList(lists, cz) {
        var max = 0;
        var num = 0;
        var list = lists.join(",").split(",");
        for (var i = 0; i < list.length; i++) {
            if (typeof (list[i]) == "object") {
                num = eval(list[i].join("+"));
            } else {
                num = list[i];
            }
            num = Math.abs(num);
            if (num > max) {
                max = num;
            }
        }
        if (cz == null) {
            cz = 0;
        }
        return max + cz;
    };

    
    
    function GetMinInList(lists, cz) {
        var min = 9999999999;
        var num = 0;
        var list = lists.join(",").split(",");
        for (var i = 0; i < list.length; i++) {
            if (typeof (list[i]) == "object") {
                num = eval(list[i].join("+"));
            } else {
                num = list[i];
            }
            num = Math.abs(num);
            if (num < min) {
                min = num;
            }
        }
        if (cz == null) {
            cz = 0;
        }
        return min - cz;
    };

    
    
    function GetColors(count, sub, desc) {
        if (sub == null)
        { sub = 0; }
        var colors = ["#4d4398", "#659942", "#ffaa14", "#ea5404", "#BFBFBF", "#1A3B69", "#FFE382", "#129CD0", "#CA6B4B", "#A64DFF"];
        return colors.slice(sub, count);
    };

   
