package hr.fer.zemris.neural.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class Curve {

    private List<Point> points;

    public Curve(List<Point> points) {
        this.points = new ArrayList<>();
        for (Point p : points) {
            this.points.add(p.copy());
        }
    }

    public List<Point> getPoints() {
        return points;
    }

    public Curve normalize() {
        Point tC = getCenter();
        points.stream().forEach(point -> point.sub(tC));

        double m = points.stream()
                .map(point -> Math.max(Math.abs(point.getX()), Math.abs(point.getY())))
                .max(Double::compare).get();

        points.stream().forEach(point -> point.iscale(m));
        return this;
    }

    private Point getCenter() {
        Point center = new Point(0, 0);
        points.forEach(p -> center.add(p));
        center.iscale(points.size());
        return center;
    }

    public double getLength() {
        double len = 0;
        Point last = points.get(0);
        for (int i = 1; i < points.size(); ++i) {
            Point cur = points.get(i);
            len += cur.distTo(last);
            last = cur;
        }
        return len;
    }

    public Curve getFeats(int m) {
        double space = getLength() / (m-1);
        double curLen = 0;

        List<Point> newPoints = new ArrayList<>();

        Point last = points.get(0);
        newPoints.add(last);
        for (int i = 1; i < points.size(); ++i) {
            Point cur = points.get(i);
            curLen += cur.distTo(last);
            if (curLen >= space) {
                newPoints.add(cur);
                curLen = 0;
            }
            last = cur;
        }

        return new Curve(newPoints);
    }

    public double[] flatten() {
        return points.stream().flatMapToDouble(point -> DoubleStream.of(point.getX(), point.getY())).toArray();
    }
}
