function show_selectedImage() {
    /// get select files.
    var selected_files = document.getElementById("select_files").files;

    for (var file of selected_files) {
        console.log(file.webkitRelativePath);
        /// read file content.
        var reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onloadend = function () {
            /// deal data.
            var img = new Image();
            /// after loader, result storage the file content result.
            img.src = this.result;
            img.onload = function () {
                var canvas = document.getElementById("image_canvas");
                var w = canvas.width;
                var h = canvas.height;
                canvas.width = w;
                canvas.height = h;
                var ctx = canvas.getContext('2d');
                ctx.scale(0.4, 0.3);
                ctx.drawImage(this, 0, 0, 750, 500);
            }
        }
    }
}

window.onload = function () {
    const str = document.getElementById("diagnosis").innerText;
    switch (str) {
        case "诊断结果：AK-光化性角化病":
            document.getElementById("explanation").innerText = "光化性角化病是一种职业病，主要受日光、紫外线、放射性热能以及沥青或煤及其提炼而物诱发本病。" +
                "病损多见于中年以上男性日光暴露部位，如面部、耳廓、手背等。主要表现为表面粗糙，可见角化性鳞屑。揭去鳞屑，可见下方的基面红润，凹凸不平，呈乳头状。";
            document.getElementById("suggestions").innerText = "口服：B-顺维甲酸；芳香维甲酸依曲替酯\n外涂：氨苯甲酸制剂；维甲酸软膏；氟尿嘧啶软膏"
            break;
        case "诊断结果：BCC-基底细胞癌":
            document.getElementById("explanation").innerText = "基底细胞癌多见于老年人，好发于头、面、颈及手背等处。最初是皮肤色到暗褐色浸润的小结节，较典型者为蜡样、半透明状结节，有高起卷曲的边缘。" +
                "中央开始破溃，结黑色坏死性痂，中心坏死向深部组织扩展蔓延，呈大片状侵袭性坏死，可以深达软组织和骨组织。";
            document.getElementById("suggestions").innerText = "手术切除；\nX线照射；\n激光治疗；";
            break;
        case "诊断结果：BKL-良性角化病":
            document.getElementById("explanation").innerText = "角化病的病因是角化毛孔被角栓闭塞，呈毛孔性角化小丘疹。" +
                "部分病人有甲状腺机能低下，或有库辛氏症候群。也有一部分的病人是使用皮质类固醇以后，才发生此种皮肤病。常见于异位性倾向的病人，或遗传性，多发于同一家族。";
            document.getElementById("suggestions").innerText = "口服：青霉胺\n" +
                "外涂：二巯基丙醇软膏；皮质类固醇霜\n" +
                "注射：硫代硫酸钠（静注）；二巯基丙磺酸钠（肌注）";
            break;
        case "诊断结果：DF-皮肤纤维瘤":
            document.getElementById("explanation").innerText = "皮肤纤维瘤是成纤维细胞或组织细胞灶性增生引致的一种真皮内的良性肿瘤。" +
                "本病可发生于任何年龄，中青年多见，女性多于男性。可自然发生或外伤后引起。黄褐色或淡红色的皮内丘疹或结节是本病的临床特征。病损生长缓慢，长期存在，极少自行消退。";
            document.getElementById("suggestions").innerText = "一般不需治疗，少数损害数年内可消退。（若单个损害有疼痛引起病人痛苦时可行手术切除。皮质类固醇激素皮内注射有一定的疗效）";
            break;
        case "诊断结果：MEL-黑色素瘤":
            document.getElementById("explanation").innerText = "黑色素瘤，通常是指恶性黑色素瘤，是黑色素细胞来源的一种高度恶性的肿瘤，多发生于皮肤，也可见于黏膜和内脏。" +
                "好发于成人，极少见于儿童。 恶性黑色素瘤可由先天性或获得性良性黑素细胞痣演变而成，或由发育不良性痣恶变而来，也可以是新发生的。";
            document.getElementById("suggestions").innerText = "手术切除\n药物：干扰素；化疗放射治疗";
            break;
        case "诊断结果：NV-黑素细胞痣":
            document.getElementById("explanation").innerText = "黑色素痣（melanocytic nevi）是由一群良性的黑色素细胞，聚集在表皮与真皮的交界产生的。" +
                "黑色素细胞可能会分布在网状真皮下部，结缔组织束之间，围绕皮肤的其它附属器官如汗腺、毛囊、血管、神经等等，偶尔还会延伸到皮下脂肪。";
            document.getElementById("suggestions").innerText = "在医学上黑色素痣属于良性的肿瘤，一般而言并不需要治疗。（想要去除黑色素痣，常用的方式有，手术切除、雷射治疗、电波刀切除等。）";
            break;
        case "诊断结果：SCC-鳞状细胞癌":
            document.getElementById("explanation").innerText = "鳞状细胞癌，又名表皮癌，是发生于表皮或附属器细胞的一种恶性肿瘤，癌细胞有不同程度的角化。" +
                "多见于有鳞状上皮覆盖的部位，如皮肤、口腔、唇、食管等处。此外，有些部位如支气管、膀胱、肾盂等处虽无鳞状上皮覆盖，但可通过鳞状上皮化生而形成鳞状细胞癌。";
            document.getElementById("suggestions").innerText = "手术切除为主\n中晚期以手术、放疗和化疗方式综合治疗";
            break;
        case "诊断结果：VASC-血管病变":
            document.getElementById("explanation").innerText = "血管病变是皮肤和下层组织相对常见的异常，影响多达50%的18岁及以上的女性。" +
                "血管病变包括一系列病症，无论是腿部静脉，面部静脉，血管瘤或静脉湖。蜘蛛静脉，毛细血管扩张和弥漫性发红通常是衰老，日晒或怀孕的结果，有时也是遗传造成的。";
            document.getElementById("suggestions").innerText = "激光和基于光的设备是安全有效地消除和减少不必要的血管问题的可行治疗选择。";
            break;
        default:
            document.getElementById("explanation").innerText = "出现错误，请联系管理员";
            break;
    }
}