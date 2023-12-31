package com.martomate.hexacraft.world.render.segment

import scala.collection.mutable

class ChunkSegs extends mutable.Iterable[Segment] {

  private val segments: mutable.TreeSet[Segment] = mutable.TreeSet.empty { (s1, s2) =>
    if (s1.overlaps(s2)) 0
    else s1.start - s2.start
  }
  private val segmentsContain: mutable.TreeMap[Segment, Segment] = mutable.TreeMap.empty { (s1, s2) =>
    if (s2.contains(s1)) 0
    else s1.start - s2.start
  }
  private var _totalLength = 0

  protected def _add(seg: Segment): Unit = {
    require(segments.add(seg), s"$seg cannot be added")
    segmentsContain(seg) = seg
    _totalLength += seg.length
  }
  protected def _remove(seg: Segment): Unit = {
    require(segmentsContain.remove(seg).isDefined, s"$seg cannot be removed")
    segments.remove(seg)
    _totalLength -= seg.length
  }

  def add(seg: Segment): Unit = {
    require(!segments.contains(seg))
    _add(seg)
  }

  /**
   * segment [a, b] has to either exist as [a, b] or be part of a bigger existing segment [c, d], c
   * <= a, d >= b
   */
  def remove(seg: Segment): Boolean =
    containedInSegments(seg) match {
      case Some(other) =>
        val len1 = seg.start - other.start
        val len2 = other.length - len1 - seg.length

        _remove(other)
        if (len1 > 0) _add(Segment(other.start, len1))
        if (len2 > 0) _add(Segment(other.start + other.length - len2, len2))

        true
      case None =>
        false
    }

  private def containedInSegments(seg: Segment): Option[Segment] =
    segmentsContain.get(seg)

  def totalLength: Int = _totalLength

  override def iterator: Iterator[Segment] = segments.iterator

  def firstSegment(): Segment = segments.firstKey
  def lastSegment(): Segment = segments.last
}
