@echo off
cat "F:\Users\Martin Pecka\Desktop\origData.txt" | sed s/\([0-9]\)\.\([0-9]*\)E-30/0.\1\2E-29/g | sed s/\([0-9]\)\.\([0-9]*\)E-29/0.\1\2E-28/g | sed s/\([0-9]\)\.\([0-9]*\)E-28/0.\1\2E-27/g | sed s/\([0-9]\)\.\([0-9]*\)E-27/0.\1\2E-26/g | sed s/\([0-9]\)\.\([0-9]*\)E-26/0.\1\2E-25/g | sed s/\([0-9]\)\.\([0-9]*\)E-25/0.\1\2E-24/g | sed s/\([0-9]\)\.\([0-9]*\)E-24/0.\1\2E-23/g | sed s/\([0-9]\)\.\([0-9]*\)E-23/0.\1\2E-22/g | sed s/\([0-9]\)\.\([0-9]*\)E-22/0.\1\2E-21/g | sed s/\([0-9]\)\.\([0-9]*\)E-21/0.\1\2E-20/g | sed s/\([0-9]\)\.\([0-9]*\)E-20/0.\1\2E-19/g | sed s/\([0-9]\)\.\([0-9]*\)E-19/0.\1\2E-18/g | sed s/\([0-9]\)\.\([0-9]*\)E-18/0.\1\2E-17/g | sed s/\([0-9]\)\.\([0-9]*\)E-17/0.\1\2E-16/g | sed s/\([0-9]\)\.\([0-9]*\)E-16/0.\1\2E-15/g | sed s/\([0-9]\)\.\([0-9]*\)E-15/0.\1\2E-14/g | sed s/\([0-9]\)\.\([0-9]*\)E-14/0.\1\2E-13/g | sed s/\([0-9]\)\.\([0-9]*\)E-13/0.\1\2E-12/g | sed s/\([0-9]\)\.\([0-9]*\)E-12/0.\1\2E-11/g | sed s/\([0-9]\)\.\([0-9]*\)E-11/0.\1\2E-10/g | sed s/\([0-9]\)\.\([0-9]*\)E-10/0.\1\2E-9/g | sed s/\([0-9]\)\.\([0-9]*\)E-9/0.\1\2E-8/g | sed s/\([0-9]\)\.\([0-9]*\)E-8/0.\1\2E-7/g | sed s/\([0-9]\)\.\([0-9]*\)E-7/0.\1\2E-6/g | sed s/\([0-9]\)\.\([0-9]*\)E-6/0.\1\2E-5/g | sed s/\([0-9]\)\.\([0-9]*\)E-5/0.\1\2E-4/g | sed s/\([0-9]\)\.\([0-9]*\)E-4/0.\1\2E-3/g | sed s/\([0-9]\)\.\([0-9]*\)E-3/0.\1\2E-2/g | sed s/\([0-9]\)\.\([0-9]*\)E-2/0.\1\2E-1/g | sed s/\([0-9]\)\.\([0-9]*\)E-1/0.\1\2/g | sed "s/Triangle3d \[(\([-0-9.]*\), \([-0-9.]*\), \([-0-9.]*\)), (\([-0-9.]*\), \([-0-9.]*\), \([-0-9.]*\)), (\([-0-9.]*\), \([-0-9.]*\), \([-0-9.]*\))\]/Scale[Polygon[{{\1,\2,\3},{\4,\5,\6},{\7,\8,\9}}],{1,1,1}]/g" | sed "s/]]], P/]]]\nP/g" | cat -n | sed "s/^\s*\([0-9]*\)\s*/RGBColor[Mod[\1*10,255]\/255,Mod[\1*20,255]\/255,Mod[\1*30,255]\/255,0.5], /g" | sed "s/Polygon3d \[\[//g" | sed "s/]]]/]/g" | tr "\n" ", " | sed "s/^\(.*\),$/Graphics3D[{\1}, PlotRange -> {{-2, 2}, {-2, 2}, All}, Axes -> True, AxesLabel -> {x,y,z}]/" > "F:\Users\Martin Pecka\Desktop\origData.nb"
start "E:\Programy\Mathematica\mathematica.exe" "F:\Users\Martin Pecka\Desktop\origData.nb"
