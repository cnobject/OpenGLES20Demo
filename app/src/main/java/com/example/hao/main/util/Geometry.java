package com.example.hao.main.util;

/**
 * Created by hao on 17-4-28.
 * 几何类
 */

public class Geometry {

    public static class Point {
        public final float x, y, z;

        public Point(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Point translateY(float distance) {
            return new Point(x, y + distance, z);
        }

        public Point translate(Vector vector) {
            return new Point(x + vector.x,
                    y + vector.y,
                    z + vector.z);
        }
    }

    public static class Circle {
        public final Point center;
        public final float radius;

        public Circle(Point point, float radius) {
            center = point;
            this.radius = radius;
        }

        public Circle scale(float scale) {
            return new Circle(center, radius * scale);
        }
    }

    public static class Cylinder {
        public final Point center;
        public final float radius;
        public final float height;

        public Cylinder(Point point, float radius, float height) {
            center = point;
            this.radius = radius;
            this.height = height;
        }
    }

    public static class Ray {
        public final Point point;
        public final Vector vector;

        public Ray(Point point, Vector vector) {
            this.point = point;
            this.vector = vector;
        }
    }

    public static class Vector {
        public final float x, y, z;

        public Vector(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public float length() {
            return (float) Math.sqrt(x * x + y * y + z * z);
        }

        //两个向量的乘积
        public Vector crossProduct(Vector other) {
            return new Vector(
                    y * other.z - z * other.y,
                    z * other.x - x * other.z,
                    x * other.y - y * other.x
            );
        }

        public float dotProduct(Vector other) {
            return x * other.x + y * other.y + z * other.z;
        }

        public Vector scale(float f) {
            return new Vector(
                    x * f,
                    y * f,
                    z * f
            );
        }

        public Vector normalize() {
            return scale(1 / length());
        }
    }

    public static class Sphere {
        public final Point center;
        public final float radius;

        public Sphere(Point center, float radius) {
            this.center = center;
            this.radius = radius;
        }

    }

    public static class Plane {
        public final Point point;
        public final Vector normal;

        public Plane(Point point, Vector normal) {
            this.point = point;
            this.normal = normal;
        }
    }

    public static Vector vectorBetween(Point from, Point to) {
        return new Vector(
                to.x - from.x,
                to.y - from.y,
                to.z - from.z
        );
    }

    public static boolean intersects(Sphere sphere, Ray ray) {
        return distanceBetween(sphere.center, ray) < sphere.radius;
    }

    private static float distanceBetween(Point point, Ray ray) {
        Vector p1ToPoint =  vectorBetween(ray.point, point);
        Vector p2ToPoint =  vectorBetween(ray.point.translate(ray.vector), point);

        //两个向量乘积得到的第三个向量的长度正好是两个向量构成的三角形面积的2倍
        float areaOfTriangleTimesTow = p1ToPoint.crossProduct(p2ToPoint).length();
        //三角形底边的长度
        float lengthOfBase = ray.vector.length();
        return areaOfTriangleTimesTow / lengthOfBase;
    }

    public static Point intersectionPoint(Ray ray, Plane plane) {
        Vector rayToPlaneVector = vectorBetween(ray.point, plane.point);

        //缩放因子
        float scaleFactor = rayToPlaneVector.dotProduct(plane.normal) / ray.vector.dotProduct(plane.normal);
        return ray.point.translate(ray.vector.scale(scaleFactor));
    }
}
